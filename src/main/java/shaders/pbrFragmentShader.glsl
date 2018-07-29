#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[6];
in vec3 toCameraVector;
in float visibility;
in vec4 worldPosition;
out vec4 out_Color;

uniform vec3 lightColour[6];
uniform float lightAttenuation[6];

uniform sampler2D albedo;
uniform sampler2D normalMap;
uniform sampler2D metallicMap;
uniform sampler2D roughnessMap;
uniform sampler2D aoMap;

uniform vec3 skyColour;

const float kPi = 3.14159265;
const float kShininess = 16.0;


vec3 getNormalFromMap()
{
    vec3 tangentNormal = texture(normalMap, pass_textureCoords).xyz * 2.0 - 1.0;

    vec3 Q1  = dFdx(worldPosition.xyz);
    vec3 Q2  = dFdy(worldPosition.xyz);
    vec2 st1 = dFdx(pass_textureCoords);
    vec2 st2 = dFdy(pass_textureCoords);

    vec3 N   = normalize(surfaceNormal);
    vec3 T  = normalize(Q1*st2.t - Q2*st1.t);
    vec3 B  = -normalize(cross(N, T));
    mat3 TBN = mat3(T, B, N);

    return normalize(TBN * tangentNormal);
}

vec3 fresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

float DistributionGGX(vec3 N, vec3 H, float roughness)
{
    float a      = roughness*roughness;
    float a2     = a*a;
    float NdotH  = max(dot(N, H), 0.0);
    float NdotH2 = NdotH*NdotH;

    float num   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = kPi * denom * denom;
    return num / denom;
}

float GeometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float num   = NdotV;
    float denom = NdotV * (1.0 - k) + k;
    return num / denom;
}
float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness)
{
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2  = GeometrySchlickGGX(NdotV, roughness);
    float ggx1  = GeometrySchlickGGX(NdotL, roughness);

    return ggx1 * ggx2;
}

void main(void) {
    vec4 rawSample = texture(albedo, pass_textureCoords);
	vec3 textureColour = pow(rawSample.rgb, vec3(2.2));
	vec3 normal = getNormalFromMap();

	float metallic = texture(metallicMap, pass_textureCoords).r;
	float roughness = texture(roughnessMap, pass_textureCoords).r;
	float ao = texture(aoMap, pass_textureCoords).r;

	//normalizes the surface normal and the pixel-to-light vector
	vec3 unitNormal = normalize(surfaceNormal); //N
	vec3 V = normalize(toCameraVector - vec3(worldPosition));
    vec3 Lo = vec3(0.0);
	//computing diffuse lighting
	for(int i = 0; i < 6; i++) {
	    vec3 L = normalize(toLightVector[i]);
	    vec3 H = normalize(L + V);

	    float distance = length(toLightVector[i]);

        float attFactor = lightAttenuation[i] * distance * distance;

        vec3 radiance = lightColour[i];

        vec3 F0 = vec3(0.04);
        F0 = mix(F0, vec3(textureColour), metallic);
        vec3 F = fresnelSchlick(max(dot(H, V), 0.0), F0);

        float NDF = DistributionGGX(unitNormal, H, clamp(roughness,0.03, 1.0));
        float G = GeometrySmith(unitNormal, V, L, roughness);

        vec3 numerator    = NDF * G * F;
        float denominator = 4.0 * max(dot(unitNormal, V), 0.0) * max(dot(unitNormal, L), 0.0);
        vec3 specular     = numerator / max(denominator, 0.001);

        vec3 kS = F;
        vec3 kD = vec3(1.0) - kS;

        kD *= 1.0 - metallic;

        float NdotL = max(dot(unitNormal, L), 0.0);
        Lo += (kD * vec3(textureColour) / kPi + specular) * radiance * NdotL;
	}
	//sampling the texure
	//if the pixel is transparent discard

	if(rawSample.a < 0.5) {
		discard;
	}

	//mixing all the lighting
	//out_Color = vec4(diffuse, 1.0) * textureColour + vec4(specular, 1.0);
	out_Color = vec4(0.03) * vec4(textureColour, rawSample.a) * ao + vec4(Lo,1.0);
	out_Color = out_Color / (out_Color + vec4(1.0));
	out_Color = pow(out_Color, vec4(1.0/ 2.2));
	//creating fog effect for object in the distance
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}