#version 450 core

const int MAX_LIGHTS = 6;
const bool BLINN = true;

const float kPi = 3.14159265;
const float kShininess = 16.0;

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[MAX_LIGHTS];
in vec3 toCameraVector;
in float visibility;
in vec4 worldPosition;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColour[MAX_LIGHTS];
uniform vec3 attenuation[MAX_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;

void main() {

	vec4 blendMapColour = texture(blendMap, pass_textureCoords);
	
	float backTextureAmount= 1 - (blendMapColour.r + blendMapColour.g + blendMapColour.b);
	vec2 tiledCoords = pass_textureCoords * 40.0;
	vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;

	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < MAX_LIGHTS; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);

		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);

        float spec = 0.0;
        if(BLINN) {
            const float kEnergyConservation = (8.0 + kShininess) / (8.0*kPi);

            vec3 halfwayDir = normalize(unitLightVector + unitVectorToCamera);

            spec = kEnergyConservation * pow(max(dot(unitNormal, halfwayDir), 0.0), kShininess);
          } else {
            const float kEnergyConservation = (2.0 + kShininess) / (2.0*kPi);

		    vec3 lightDirection = -unitLightVector;
		    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

		    spec = kEnergyConservation * pow(max(dot(unitVectorToCamera, reflectedLightDirection), 0.0), kShininess);
        }

		totalDiffuse = totalDiffuse + (brightness * lightColour[i]) / attFactor;
		totalSpecular = totalSpecular + (spec * reflectivity * lightColour[i]) / attFactor;
	}

	totalDiffuse = max(totalDiffuse, 0.2);

	out_Color = vec4(totalDiffuse, 1.0) * totalColour + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}