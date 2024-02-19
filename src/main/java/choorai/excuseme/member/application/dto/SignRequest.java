package choorai.excuseme.member.application.dto;

public record SignRequest(String id, String password, String name, String gender, String birthDate,
                          String phoneNumber) {

}
