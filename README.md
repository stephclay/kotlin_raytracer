# Ray Tracing in One Weekend

This is my Kotlin implementation of the code for the class Ray Tracing in One Weekend
and its follow-on cources located at the following URLs:

* https://raytracing.github.io/books/RayTracingInOneWeekend.html
* https://raytracing.github.io/books/RayTracingTheNextWeek.html
* https://raytracing.github.io/books/RayTracingTheRestOfYourLife.html

## Notes
* This project was started as a way to improve my Kotlin skills, so some code may not be as
  idiomatic as some people might expect.
* After some profiling, ThreadLocalRandom was chosen as the RNG instead of SecureRandom, which
  has much better performance, even for non-threaded cases.

## AI Notice
* None of the code in the application was written by AI, since the point was to further
  develop my skills in Kotlin 
* AI was used in a small number of cases as a research tool to help resolve unusual build
  issues and to identify technology options, such as different ways to load YAML.
