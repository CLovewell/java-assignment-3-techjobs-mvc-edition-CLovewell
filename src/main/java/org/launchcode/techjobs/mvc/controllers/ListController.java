package org.launchcode.techjobs.mvc.controllers;


import org.launchcode.techjobs.mvc.models.Job;
import org.launchcode.techjobs.mvc.models.JobData;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "list")
public class ListController {

    static HashMap<String, String> columnChoices = new HashMap<>();
    static HashMap<String, Object> tableChoices = new HashMap<>();
    static ArrayList<HashMap<Expression, String>> jobFields = new ArrayList<>();
    static ExpressionParser parser = new SpelExpressionParser();

    public ListController () {
        columnChoices.put("all", "All");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("positionType", "Position Type");
        columnChoices.put("coreCompetency", "Skill");

        tableChoices.put("employer", JobData.getAllEmployers());
        tableChoices.put("location", JobData.getAllLocations());
        tableChoices.put("positionType", JobData.getAllPositionTypes());
        tableChoices.put("coreCompetency", JobData.getAllCoreCompetencies());

        HashMap<Expression, String> idField = new HashMap<>();
        idField.put(parser.parseExpression("id"), "ID");
        HashMap<Expression, String> nameField = new HashMap<>();
        nameField.put(parser.parseExpression("name"), "Name");
        HashMap<Expression, String> employerField = new HashMap<>();
        employerField.put(parser.parseExpression("employer"), "Employer");
        HashMap<Expression, String> locationField = new HashMap<>();
        locationField.put(parser.parseExpression("location"), "Location");
        HashMap<Expression, String> positionTypeField = new HashMap<>();
        positionTypeField.put(parser.parseExpression("positionType"), "Position Type");
        HashMap<Expression, String> coreCompetencyField = new HashMap<>();
        coreCompetencyField.put(parser.parseExpression("coreCompetency"), "Skill");
        jobFields.add(idField);
        jobFields.add(nameField);
        jobFields.add(employerField);
        jobFields.add(locationField);
        jobFields.add(positionTypeField);
        jobFields.add(coreCompetencyField);
    }

    @GetMapping(value = "")
    public String list(Model model) {
        model.addAttribute("columns", columnChoices);
        model.addAttribute("tableChoices", tableChoices);
        model.addAttribute("employers", JobData.getAllEmployers());
        model.addAttribute("locations", JobData.getAllLocations());
        model.addAttribute("positions", JobData.getAllPositionTypes());
        model.addAttribute("skills", JobData.getAllCoreCompetencies());

        return "list";
    }

    @GetMapping(value = "jobs")
    public String listJobsByColumnAndValue(Model model, @RequestParam String column,
                                           @RequestParam(required = false) String value) {
        ArrayList<Job> jobs;
        if (column.equals("all")){
            jobs = JobData.findAll();
            model.addAttribute("title", "All Jobs");
        } else {
            jobs = JobData.findByColumnAndValue(column, value);
            model.addAttribute("title", "Jobs with " + columnChoices.get(column) + ": " + value);
        }
        model.addAttribute("jobs", jobs);
        model.addAttribute("fields", jobFields);

        return "list-jobs";
    }
}
