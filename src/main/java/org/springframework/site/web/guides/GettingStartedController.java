package org.springframework.site.web.guides;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.site.domain.guides.GettingStartedGuide;
import org.springframework.site.domain.guides.GettingStartedService;
import org.springframework.site.web.NavSection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.site.web.guides.GettingStartedController.GUIDES_ROOT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

@Controller
@RequestMapping(GUIDES_ROOT)
@NavSection("guides")
public class GettingStartedController {

	public static final String GUIDES_ROOT = "/guides/gs";
	private static final String TRAILING_SLASH = "/";
	private static final String SHOW_GUIDE = "/{guideId}" + TRAILING_SLASH;

	static String getPath(GettingStartedGuide guide) {
		return GUIDES_ROOT + "/" + guide.getGuideId() + TRAILING_SLASH;
	}

	private GettingStartedService service;

	@Autowired
	public GettingStartedController(GettingStartedService service) {
		this.service = service;
	}

	@RequestMapping(value = SHOW_GUIDE, method = { GET, HEAD })
	public String viewGuide(@PathVariable String guideId, Model model) {
		model.addAttribute("guideSlug", guideId);
		model.addAttribute("guide", this.service.loadGuide(guideId));
		return "guides/gs/guide";
	}

	@RequestMapping(value = "/{guideSlug}/images/{name:[a-zA-Z0-9._-]+}", method = { GET, HEAD })
	public ResponseEntity<byte[]> loadImage(@PathVariable String guideSlug,
											@PathVariable("name") String imageName) {
		byte[] image = this.service.loadImage(guideSlug, imageName);
		return new ResponseEntity<>(image, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = { GET, HEAD })
	public String listGuides(Model model) {
		model.addAttribute("guides", this.service.listGuides());
		return "guides/gs/list";
	}

}
