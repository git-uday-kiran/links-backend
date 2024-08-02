package links.backend;

import com.fasterxml.jackson.annotation.JsonValue;

enum TopicTag {
	REFERENCE, BLOG, BOOK, PDF, VIDEO, PODCAST, TUTORIAL, CAREER_GUIDANCE, CONFERENCE, OTHER;

	@Override
	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}

}
