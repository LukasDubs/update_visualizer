#version 330 core

layout (location = 0) in vec4 pos;
layout (location = 1) in vec4 color;

uniform mat4 matProj;
uniform mat4 matModel;

out vec4 v_Color;

void main() {
    gl_Position = matProj * matModel * pos;
    v_Color = color;
}