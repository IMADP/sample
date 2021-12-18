package com.sample.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SampleApiController
 *
 * @author Anthony DePalma
 */
@Controller
public class SampleApiController {

	@GetMapping
	public String index() {
		return "redirect:/swagger-ui/";
	}

}