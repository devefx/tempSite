#ifdef GL_ES
precision lowp float;
#endif

varying vec2 v_texCoord;

void main()
{
    gl_FragColor =  texture2D(CC_Texture0, v_texCoord);
}