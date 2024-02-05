package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.model.Candidate;
import com.model.Role;
import com.model.User;
import com.service.CandidateService;
import com.service.RoleService;
import com.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userserv;

	@Autowired
	private RoleService roleserv;

	@GetMapping("/")
	public String index(Model m) {

		m.addAttribute("title", "Home");
		return "home";
	}

	@GetMapping("/signin")
	public String signin(Model m) {
		m.addAttribute("title", "Signin");
		return "signin";
	}

	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("title", "Registration");
		return "register";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About");
		return "about";
	}
	@Autowired
	private UserService userServ;

	@Autowired
	private CandidateService cndServ;
    @GetMapping("/results")
public String viewcandidates(Model m) {
    List<Candidate> candidates = cndServ.getAllCandidates();
    m.addAttribute("candidates", candidates);
    m.addAttribute("title", "All Votes Details");

    // view total number of votes
    int c1 = 0;
    int c2 = 0;
    int c3 = 0;
    int c4 = 0;

    List<Candidate> listc = cndServ.getAllCandidates();
    for (Candidate v : listc) {
        if (!v.getCandidate1().equals(""))
            c1 = c1 + 1;
        if (!v.getCandidate2().equals(""))
            c2 = c2 + 1;
        if (!v.getCandidate3().equals(""))
            c3 = c3 + 1;
        if (!v.getCandidate4().equals(""))
            c4 = c4 + 1;
    }

    m.addAttribute("c1", c1);
    m.addAttribute("c2", c2);
    m.addAttribute("c3", c3);
    m.addAttribute("c4", c4);

    // Determine the leading candidate
    String leadingCandidate = determineLeadingCandidate(c1, c2, c3, c4);
    m.addAttribute("leadingCandidate", leadingCandidate);

    // Determine the winner
    String winner = determineWinner(c1, c2, c3, c4);
    m.addAttribute("winner", winner);

    return "results";
}

private String determineLeadingCandidate(int c1, int c2, int c3, int c4) {
    // Implement logic to determine the leading candidate
    int maxVotes = Math.max(c1, Math.max(c2, Math.max(c3, c4)));

    if (maxVotes == c1) {
        return "Candidate 1";
    } else if (maxVotes == c2) {
        return "Candidate 2";
    } else if (maxVotes == c3) {
        return "Candidate 3";
    } else {
        return "Candidate 4";
    }
}

private String determineWinner(int c1, int c2, int c3, int c4) {
    // Implement logic to determine the winner
    int maxVotes = Math.max(c1, Math.max(c2, Math.max(c3, c4)));

    if (maxVotes == c1 && noTie(c1, c2, c3, c4)) {
        return "Candidate 1";
    } else if (maxVotes == c2 && noTie(c1, c2, c3, c4)) {
        return "Candidate 2";
    } else if (maxVotes == c3 && noTie(c1, c2, c3, c4)) {
        return "Candidate 3";
    } else if (maxVotes == c4 && noTie(c1, c2, c3, c4)) {
        return "Candidate 4";
    }else {
        return getWinnerWithMostVotes(c1, c2, c3, c4);
    }
}
private String getWinnerWithMostVotes(int c1, int c2, int c3, int c4) {
    // Implement logic to get the winner with the most votes in case of a tie
    int maxVotes = Math.max(c1, Math.max(c2, Math.max(c3, c4)));

    if (maxVotes == c1) {
        return "Candidate 1";
    } else if (maxVotes == c2) {
        return "Candidate 2";
    } else if (maxVotes == c3) {
        return "Candidate 3";
    } else {
        return "Candidate 4";
    }
}
private boolean noTie(int c1, int c2, int c3, int c4) {
    // Implement logic to check if there is no tie
    return c1 != c2 && c1 != c3 && c1 != c4 && c2 != c3 && c2 != c4 && c3 != c4;
}

	@GetMapping("/updates")
	public String update(Model m) {
		m.addAttribute("title", "updates");
		List<Candidate> candidates = cndServ.getAllCandidates();
    m.addAttribute("candidates", candidates);
    m.addAttribute("title", "All Votes Details");

    // view total number of votes
    int c1 = 0;
    int c2 = 0;
    int c3 = 0;
    int c4 = 0;

    List<Candidate> listc = cndServ.getAllCandidates();
    for (Candidate v : listc) {
        if (!v.getCandidate1().equals(""))
            c1 = c1 + 1;
        if (!v.getCandidate2().equals(""))
            c2 = c2 + 1;
        if (!v.getCandidate3().equals(""))
            c3 = c3 + 1;
        if (!v.getCandidate4().equals(""))
            c4 = c4 + 1;
    }

    m.addAttribute("c1", c1);
    m.addAttribute("c2", c2);
    m.addAttribute("c3", c3);
    m.addAttribute("c4", c4);

    // Determine the leading candidate
    String leadingCandidate = determineLeadingCandidate(c1, c2, c3, c4);
    m.addAttribute("leadingCandidate", leadingCandidate);

		return "updates";
	}

	@PostMapping("/createuser")
	public String createuser(@ModelAttribute User user, HttpSession session) {
		int i = 0;

		try {
			String email = user.getEmail();

			if (userserv.getUserByEmail(email) != null) {
				i++;
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		if (i >= 1) {
			session.setAttribute("msg", "Registration Failed, Please try different email id");
			return "redirect:/register";
		}

		// Check if the user's age is greater than 18
		if (user.getAge() <= 18) {
			session.setAttribute("msg", "SORRY, YOU ARE NOT ELIGIBLE. VOTER MUST BE 18 YEARS OR OLDER.");
			return "redirect:/register";
		}

		List<Role> r = new ArrayList<>();
		r.add(roleserv.getRoleByName("ROLE_USER"));

		user.setRoles(r);
		userserv.addUser(user);

		session.setAttribute("msg", "Registration Successful...");

		return "redirect:/register";
	}

}
