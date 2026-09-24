package com.devtrack.infrastructure.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Exercises {@link GlobalExceptionHandler} in isolation, against a throwaway
 * controller, for the branches not already covered incidentally by the
 * per-entity controller integration tests (illegal-argument and the generic
 * fallback for unexpected exceptions).
 */
class GlobalExceptionHandlerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new ThrowingController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void mapsIllegalArgumentExceptionToBadRequest() throws Exception {
        mockMvc.perform(get("/throw/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", org.hamcrest.Matchers.is(400)))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.is("invalid input")));
    }

    @Test
    void mapsUnexpectedExceptionToInternalServerErrorWithoutLeakingItsMessage() throws Exception {
        mockMvc.perform(get("/throw/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", org.hamcrest.Matchers.is(500)))
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.is("Unexpected error")));
    }

    @RestController
    private static class ThrowingController {

        @GetMapping("/throw/illegal-argument")
        void throwIllegalArgument() {
            throw new IllegalArgumentException("invalid input");
        }

        @GetMapping("/throw/unexpected")
        void throwUnexpected() {
            throw new RuntimeException("some sensitive internal detail");
        }
    }
}
