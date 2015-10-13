uniform   float u_time;
uniform   mat4  u_MVPMatrix;

attribute float a_lifetime;
attribute vec4  a_startPosition;
attribute vec4  a_endPosition;

varying   float v_lifetime;

void main() {
    if (u_time <= a_lifetime) {
        gl_Position = u_MVPMatrix * (a_startPosition + (u_time * a_endPosition));
    } else {
        // dead, render it off the screen
        gl_Position = vec4(-1000, -1000, 0, 0);
    }

    v_lifetime = 1.0 - (u_time / a_lifetime);
    v_lifetime = clamp(v_lifetime, 0.0, 1.0);
    gl_PointSize = (v_lifetime * v_lifetime) * 40.0;
}
