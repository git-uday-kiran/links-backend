package links.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	void addLink(@RequestBody Link link) {
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
		return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage())).build();
	}

	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<ProblemDetail> runtimeException(RuntimeException exception) {
		return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage())).build();
	}

}
