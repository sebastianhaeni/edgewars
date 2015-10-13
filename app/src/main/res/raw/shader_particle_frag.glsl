precision mediump float;

uniform vec4      u_color;
varying float     v_lifetime;
uniform sampler2D s_texture;

void main() {
    vec4 texColor;
    texColor = texture2D(s_texture, gl_PointCoord);
    gl_FragColor = vec4(u_color) * texColor;
    gl_FragColor.a *= v_lifetime;
}
