/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/LocalApplicationRunner.kt
*/

package no.enterprise2.contexam.messages

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, "--spring.profiles.active=test")
}