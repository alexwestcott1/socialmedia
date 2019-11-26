package com.alexwestcott.socialmedia.repo;

import com.alexwestcott.socialmedia.domain.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository  extends ReactiveCrudRepository<Chapter, String> {
}
