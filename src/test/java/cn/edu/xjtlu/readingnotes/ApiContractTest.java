package cn.edu.xjtlu.readingnotes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springdoc.core.OpenAPIService;
import com.atlassian.oai.validator.OpenApiValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.atlassian.oai.validator.OpenApiValidationLevel.WARN;

@SpringBootTest
class ApiContractTest {

    @Autowired
    private OpenAPIService openAPIService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validateApiSpecification() throws Exception {
        String openApiSpec = objectMapper.writeValueAsString(openAPIService.getOpenAPI());
        
        OpenApiValidator validator = OpenApiValidator.forSpecification(openApiSpec)
            .withOperationValidationLevel(WARN)
            .build();
        
        validator.validate();
    }
}