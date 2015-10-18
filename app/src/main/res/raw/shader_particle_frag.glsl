precision mediump float;

uniform vec4  u_color;
varying float v_lifetime;

void main() {
    gl_FragColor = u_color;
    gl_FragColor.a *= v_lifetime;
}
