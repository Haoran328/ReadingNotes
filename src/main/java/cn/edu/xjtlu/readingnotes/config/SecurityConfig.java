@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // 添加JWT过滤器配置
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/register", "/login").permitAll()
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/**/log/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
    }
}