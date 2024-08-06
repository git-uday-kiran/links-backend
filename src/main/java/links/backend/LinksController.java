package links.backend;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class LinksController {

	private final LinkService service;

	@GetMapping
	List<Link> getLinks() {
		return service.getLinks();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	void addLink(@RequestBody @Valid Link link) {
		service.add(link);
	}

	@DeleteMapping("{linkId}")
	void deleteLink(@PathVariable int linkId) {
		service.delete(linkId);
	}

	@PutMapping
	void updateLink(@RequestBody Link link) {
		service.update(link);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<ProblemDetail> httpMessageNotReadableException(HttpMessageNotReadableException exception) {
		log.error(exception);
		return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage())).build();
	}

	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<ProblemDetail> runtimeException(RuntimeException exception) {
		log.error(exception);
		return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage())).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ProblemDetailJson handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		log.error(exception);
		ProblemDetailJson body = ProblemDetailJson.forStatus(HttpStatus.BAD_REQUEST);
		body.setDetail("validation failed on some fields");
		body.setTitle("Validation Failed");

		List<Object> jsonFieldErrors = new ArrayList<>();
		body.put("field_errors", jsonFieldErrors);

		for (FieldError fieldError : exception.getFieldErrors()) {
			Map<String, Object> jsonFieldError = new HashMap<>();
			jsonFieldErrors.add(jsonFieldError);
			jsonFieldError.put("field", fieldError.getField());
			jsonFieldError.put("message", fieldError.getDefaultMessage());
			jsonFieldError.put("rejected_value", fieldError.getRejectedValue());
			jsonFieldError.put("type_mismatch", fieldError.isBindingFailure());
		}
		return body;
	}

}
