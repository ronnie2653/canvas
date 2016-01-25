README for Springer Code Test
(Created by: Sandor Nagy)

Description: Program to accomplish simple character based drawing according to user input.

The main idea is that the program stores the actual state of the canvas (represented as a 2D Array of String) and updates it according to the user input. Then the actual representation will be the updated one.  

The main principles for development:
 - Keep it simple: no over-engineering, no speculation about future components (and add unnecessary complexity).
 - Error handling: exception thrown as soon as possible and make to program handle user input gracefully (no crash in case of  bad input just helpful message). Using Option for avoiding null pointer exceptions.

Project created with Intellij Idea 15, using Gradle as build tool and git as Version Controll.

App can be run with gradle run task (defined in build.gradle).


	
