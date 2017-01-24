/**
 * The amount of FPS to run the OpenGL overlay at.
 *
 * Usually you want to match this up with your monitor's refresh rate.
 */
val OPENGL_FPS = 60

/**
 * Whether or not to use V-sync (vertical sync) for the OpenGL overlay.
 *
 * Usually you want to use V-sync for the overlay if you use V-sync in game.
 */
val OPENGL_VSYNC = false

/**
 * The amount of MSAA antialiasing samples for the OpenGL overlay.
 *
 * Decreasing this number may help improve your FPS! Use 0 to disable.
 *
 * Valid sample amounts are 0 (disable), 2, 4, and 8.
 */
val OPENGL_MSAA_SAMPLES = 8