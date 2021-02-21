/*
This is from Andrea Arcuri's repository with modifcation:
https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/WebSecurityConfig.kt
*/

package no.enterprise2.contexam.messages

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {

        http
                .exceptionHandling().authenticationEntryPoint { req, response, e ->
                    response.setHeader("WWW-Authenticate", "cookie")
                    response.sendError(401)
                }.and()
                .authorizeRequests()
                .antMatchers("/swagger*/**", "/v3/api-docs", "/actuator/**").permitAll()
                .antMatchers("/api/messages").permitAll()
                .antMatchers("/api/messages/collection_v1_000").permitAll()
                .antMatchers("/api/messages/collection_v0_001").permitAll()
                .antMatchers("/api/messages/collection_v0_002").permitAll()
                .antMatchers("/api/messages/collection_v0_003").permitAll()
                .antMatchers(HttpMethod.GET, "/api/messages/{id}").permitAll()
                .antMatchers("/api/messages/{id}").hasRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
    }
}
