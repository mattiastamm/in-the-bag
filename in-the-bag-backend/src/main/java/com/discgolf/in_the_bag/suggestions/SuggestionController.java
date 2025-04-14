package com.discgolf.in_the_bag.suggestions;

import com.discgolf.in_the_bag.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;


    @GetMapping("/suggest")
    public ResponseEntity<List<BagSuggestionDto>> suggestForUser(@RequestParam Long bagId) {
        Long userId = jwtUtil.extractUserIdFromRequest(request);
        List<BagSuggestionDto> suggestions = suggestionService.suggestForUser(userId, bagId);
        return ResponseEntity.ok(suggestions);
    }
}

