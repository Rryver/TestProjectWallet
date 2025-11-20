package com.kolosov.testprojectwallet;

import com.kolosov.testprojectwallet.components.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@AutoConfigureMockMvc
public abstract class AbstractControllerTests extends BaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected JsonHelper jsonHelper;

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
