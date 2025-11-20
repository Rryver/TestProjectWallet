package com.kolosov.testprojectwallet.controllers.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = AbstractApiController.BASE_REST_URL)
abstract class AbstractApiController {

    public static final String BASE_REST_URL = "/api/v1";
}
