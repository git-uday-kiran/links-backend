package links.backend;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.*;

@Log4j2
@Service
class LinkService {
	private final Resource resource;
	private List<String> headers;
	private List<Link> links;

	LinkService(CsvFileProperties properties) throws IOException {
		hasLength(properties.path, "Link path must not be empty or null");
		resource = new FileSystemResource(properties.path);
		state(resource.exists(), "Links file not found, with path " + properties.path);
		init();
	}

	private void init() throws IOException {
		log.info("Loading links from {}", resource);
		var lines = Files.readAllLines(Path.of(resource.getURI()));
		log.info("lines : {}", lines);
		notEmpty(lines, "Links file should not be empty");
		headers = getHeaders(lines.get(0));
		lines.remove(0);
		links = lines.stream()
			.map(this::createLink)
			.collect(Collectors.toList());
		log.info("lines : {}", lines);
		log.info("headers : {}", headers);
		log.info("links : {}", links);
	}

	private Link createLink(String line) {
		hasLength(line, "String line must not be empty or null");
		var rowData = getRowDataAsCells(line);
		var id = Integer.parseInt(getCellData(rowData, "id"));
		var url = getCellData(rowData, "url");
		var topicTags = getCommaSeparatedValues(getCellData(rowData, "topic_tags"))
			.stream()
			.map(String::toUpperCase)
			.map(TopicTag::valueOf)
			.toList();
		var techTags = getCommaSeparatedValues(getCellData(rowData, "tech_tags"))
			.stream()
			.map(String::toUpperCase)
			.map(TechTag::valueOf)
			.toList();
		var importance = Importance.valueOf(getCellData(rowData, "importance").toUpperCase());
		var description = getCellData(rowData, "description");
		return new Link(id, url, topicTags, techTags, importance, description);
	}

	private String getCellData(List<String> rowData, String columnName) {
		var upperCaseColumn = columnName.toUpperCase();
		var columnIndex = headers.indexOf(upperCaseColumn);
		if (columnIndex == -1)
			throw new RuntimeException("column '%s' not found".formatted(upperCaseColumn));
		return rowData.get(columnIndex);
	}

	private List<String> getCommaSeparatedValues(String data) {
		return Arrays.stream(data.split(","))
			.map(String::trim)
			.toList();
	}

	private List<String> getRowDataAsCells(String line) {
		return Arrays.stream(line.split(";"))
			.map(String::trim)
			.toList();
	}

	private List<String> getHeaders(String line) {
		return getRowDataAsCells(line);
	}

	private boolean isLinkAvailable(int linkId) {
		return links.stream().anyMatch(link -> link.id() == linkId);
	}

	private void writeToFile() {
		try (var writer = new BufferedWriter(new FileWriter(resource.getFile()))) {
			writer.write(String.join(";", headers) + "\n");
			writer.write(String.join("\n", links.stream()
				.map(Link::toLine)
				.toArray(String[]::new)));
		} catch (IOException e) {
			log.error("Error while writing to the file", e);
		}
	}

	private Optional<Integer> linkLineNumber(Link link) {
		return linkLineNumber(link.id());
	}

	private Optional<Integer> linkLineNumber(int linkId) {
		for (int i = 0; i < links.size(); i++) {
			if (links.get(i).id() == linkId) {
				return Optional.of(i);
			}
		}
		return Optional.empty();
	}

	public List<Link> getLinks() {
		return Collections.unmodifiableList(links);
	}

	public void update(Link link) {
		var lineNumber = linkLineNumber(link);
		log.info("Updating link {}, lineNumber {}", link, lineNumber);
		if (lineNumber.isPresent()) {
			links.set(lineNumber.get(), link);
			writeToFile();
		}
	}

	public void delete(int linkId) {
		var lineNumber = linkLineNumber(linkId);
		log.info("Deleting linkId {}, lineNumber {}", linkId, lineNumber);
		if (lineNumber.isPresent()) {
			links.remove(lineNumber.get().intValue());
			writeToFile();
		}
	}

	public void add(Link link) {
		if (isLinkAvailable(link.id()))
			throw new RuntimeException("Link id " + link.id() + " already exists");
		log.info("Adding link {}", link);
		links.add(link);
		writeToFile();
	}

}
