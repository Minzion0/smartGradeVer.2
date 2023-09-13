package com.green.smartgradever2.email;

import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.email.model.CheckEmailDto;
import com.green.smartgradever2.email.model.EmailDto;
import com.green.smartgradever2.email.model.EmailVo;
import com.green.smartgradever2.email.model.EmailSuccessCheckDto;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.student.StudentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final StudentRepository STUDENT_REP;
    private final ProfessorRepository PROFESSOR_REP;


    /** 보낼 이메일 로직 **/
    public void sendEmail(EmailMessage message) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,  true,"UTF-8");
            mimeMessageHelper.setTo(message.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(message.getSubject()); // 메일 제목
            mimeMessageHelper.setFrom(message.getFrom());
            mimeMessageHelper.setText(message.getMessage(), true);

            javaMailSender.send(mimeMessage);
            log.info("Email send successfully!");
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException(e);
        }
    }

    /** 직접적인 이메일 내용 **/
    public EmailVo sendMail(EmailDto dto) throws Exception{
        StudentEntity entity = null;
        ProfessorEntity professor = null;


        StringBuffer msg = new StringBuffer();
        msg.append("<html>");
        msg.append("<body>");
        msg.append("<a href='http://192.168.0.144:5002'><img src='https://postfiles.pstatic.net/MjAyMzA5MDFfODQg/MDAxNjkzNTU1MDIzODIw.OOkMKxS_8VE4fyTJ9KBz97bOpjZJ6AED2dGplRpgaNQg.qOVC3dfhLZtF8RqSWgyKeGmpG-9jQoKgH7okuXI8Z6Ig.PNG.worud4227/Untitled-1.png?type=w966' /></a>");
        msg.append("<hr>");
        msg.append("<h3>OTP 등록을 위한 메일입니다.</h3>");
        msg.append("<p>모든 등록절차는 구글 otp 검색 후 -> " + "<b>" + "Google Authenicator 어플" + "</b>" + "을 다운받으셔야 합니다.</p>");
        msg.append("<p><b>Play store</b> : " + "<a href = 'https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=ko&gl=US'>" + "https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2&hl=ko&gl=US"+ "</a>" + "<p>");
        msg.append("<p><b>App store</b> : " + "<a href = 'https://apps.apple.com/kr/app/google-authenticator/id388497605'>" + "https://apps.apple.com/kr/app/google-authenticator/id388497605"+ "</a>" + "<p>");
        msg.append("<p style='margin:10px 0;'> * QR체크를 통해 등록을 원하시면 하단의 이미지로 등록을 하시면 됩니다.</p>");
        if (dto.getRole().equals("ROLE_STUDENT")) {
            entity = STUDENT_REP.findById(dto.getId()).get();
            msg.append("<img src = " + entity.getOtpUrl() + ">");
        } else if (dto.getRole().equals("ROLE_PROFESSOR")){

           professor = PROFESSOR_REP.findById(dto.getId()).get();
            msg.append("<img src = " + professor.getOtpUrl() + ">");
        }
        msg.append("<br><br>");
        msg.append("<p>* QR등록이 어려우시면 하단의 코드를 입력해주시면 됩니다</p>");

        if (dto.getRole().equals("ROLE_STUDENT")) {
            entity = STUDENT_REP.findById(dto.getId()).get();
            msg.append("<p style='background-color:#c9e1ff;display: inline-table; padding: 10px; margin: 10px 0 0;'>" + entity.getSecretKey() + "</p>");
        } else if (dto.getRole().equals("ROLE_PROFESSOR")){
            professor = PROFESSOR_REP.findById(dto.getId()).get();
            msg.append("<p style='background-color:#c9e1ff;display: inline-table; padding: 10px; margin: 10px 0 0;'>" + professor.getSecretKey() + "</p>");
        }

        msg.append("<p style='margin:10px 0;'>* 아래의 사진처럼 등록을 하시면 됩니다.</p>");
        msg.append("<img src = 'https://postfiles.pstatic.net/MjAyMzA5MDFfMTUx/MDAxNjkzNTU4MDA2OTQ3.o23K3Svmd_jttdDkkcVv0tepQauPHRat-KeO12C9uigg.4tHrxWnfPQ9MFgXUpeMAVCXdnguG8Czn__bpuHkByjYg.PNG.worud4227/usesecretkey.png?type=w966'>");
        msg.append("</body>");
        msg.append("</html>");

        if (dto.getRole().equals("ROLE_STUDENT")) {
            EmailMessage message = EmailMessage.builder()
                    .to(entity.getEmail())
                    .from("SMART_GRADE") // 누가
                    .subject("OTP 등록 안내") // 제목
                    .message(msg.toString()) //내용
                    .build();

            sendEmail(message);
        }  else if (dto.getRole().equals("ROLE_PROFESSOR")){

            EmailMessage message = EmailMessage.builder()
                    .to(professor.getEmail())
                    .from("SMART_GRADE") // 누가
                    .subject("OTP 등록 안내") // 제목
                    .message(msg.toString()) //내용
                    .build();

            sendEmail(message);
        }


        if (dto.getRole().equals("ROLE_STUDENT")) {
            entity = STUDENT_REP.findById(dto.getId()).get();
            return EmailVo.builder()
                    .id(dto.getId())
                    .role(dto.getRole())
                    .optUrl(entity.getOtpUrl())
                    .secretKey(entity.getSecretKey())
                    .build();
        }

        professor = PROFESSOR_REP.findById(dto.getId()).get();
        return EmailVo.builder()
                .id(dto.getId())
                .role(dto.getRole())
                .optUrl(professor.getOtpUrl())
                .secretKey(professor.getSecretKey())
                .build();
    }



    /** 이메일 인증 **/
    public ResponseEntity checkEmail (CheckEmailDto dto){

        String apiAddress = null;
        List<ProfessorEntity> professorMail = PROFESSOR_REP.findAll();
        if (dto.getRole().equals("ROLE_PROFESSOR")) {
            for (int i = 0; i < professorMail.size(); i++) {
                if (dto.getMail().equals(professorMail.get(i).getEmail())) {
                    return ResponseEntity.status(405).build();
                }
            }
            ResponseEntity.ok().build();
        } else if (dto.getRole().equals("ROLE_STUDENT")) {
            List<StudentEntity> studentMail = STUDENT_REP.findAll();
            for (int i = 0; i < studentMail.size(); i++) {
                if (dto.getMail().equals(studentMail.get(i).getEmail())) {
                    return ResponseEntity.status(405).build();
                }
            }
        }
        String uuid = UUID.randomUUID().toString();

        if (dto.getRole().equals("ROLE_STUDENT")) {
            StudentEntity student = STUDENT_REP.findById(dto.getIuser()).get();
            student.setEmail(uuid);
            STUDENT_REP.save(student);
            apiAddress = "?iuser=" + student.getStudentNum() + "&role=" + student.getRole() + "&uuid=" + student.getEmail();
        } else {
            ProfessorEntity professor = PROFESSOR_REP.findById(dto.getIuser()).get();
            professor.setEmail(uuid);
            PROFESSOR_REP.save(professor);
            apiAddress = "?iuser=" + professor.getIprofessor() + "&role=" + professor.getRole() + "&uuid=" + professor.getEmail();
        }
        String address = "http://192.168.0.144:5002/api/send-email/check-button" + apiAddress;

        log.info("address : {}", address);

        StringBuffer msg = new StringBuffer();
        msg.append("<html>");
        msg.append("<body>");
        msg.append("<div style='width: 500px;\n" +
                "    border: 1px solid rgba(0,0,0,.3);\n" +
                "    padding: 10px 10px 20px;\n" +
                "    border-radius: 20px;\n" +
                "    text-align: center;\n" +
                "    margin: 50px auto;\n'>" +
                "<a href='http://192.168.0.144:5002'><img src='https://postfiles.pstatic.net/MjAyMzA5MDFfODQg/MDAxNjkzNTU1MDIzODIw.OOkMKxS_8VE4fyTJ9KBz97bOpjZJ6AED2dGplRpgaNQg.qOVC3dfhLZtF8RqSWgyKeGmpG-9jQoKgH7okuXI8Z6Ig.PNG.worud4227/Untitled-1.png?type=w966' /></a>");
        msg.append("<hr>");
        msg.append("<h1>이메일 인증을 위한 메일입니다.</h1>");
        msg.append("<p>본인이 아니시라면 메일 삭제를 해주시면 됩니다.</p>");
        msg.append("<p style='margin: 0px 0 40px;'>이메일 인증을 원하신다면 <b>하단의 확인버튼</b>을 누르시면 됩니다.</p>");
        msg.append("<a href='" + address + "' style = 'font-size: 16px;\n" +
                "    text-decoration: none;\n" +
                "    color: #fff;\n" +
                "    background: #7aa5f1;\n" +
                "    padding: 10px;\n" +
                "    border-radius: 5px;'>확인하기</a></div>");
        msg.append("<p></p>");
        msg.append("</body>");
        msg.append("</html>");

        // 실제 보낼 이메일 내용
        EmailMessage message = EmailMessage.builder()
                .to(dto.getMail())
                .from("SMART_GRADE") // 누가
                .subject("SMART_GRADE 이메인 인증 확인") // 제목
                .message(msg.toString()) //내용
                .build();

        sendEmail(message);

        return ResponseEntity.ok().build();
    }

    /** 이메일 인증 확인 - api **/
    public String checkApi(Long iuser, String role){

        String uuid = UUID.randomUUID().toString();

        if (role.equals("ROLE_STUDENT")){
            StudentEntity student = STUDENT_REP.findById(iuser).get();
            String emailRom = uuid + "true";
            student.setEmail(emailRom);
            STUDENT_REP.save(student);
        } else {
            ProfessorEntity professor = PROFESSOR_REP.findById(iuser).get();
            String emailRom = uuid + "true";
            professor.setEmail(emailRom);
            PROFESSOR_REP.save(professor);
        }
        return "확인이 완료되었습니다. 전 페이지에서 이어서 작성해주시면 됩니다.";
    }

    public boolean emailSuccessCheck(EmailSuccessCheckDto dto) throws Exception{

        if (dto.getRole().equals("ROLE_STUDENT")){
            StudentEntity entity = STUDENT_REP.findById(dto.getIuser()).get();
            String email = entity.getEmail();
            String emailSubStr = email.substring(email.length()-4, email.length());

            log.info("emailSubStr : {}", emailSubStr);
            if (!emailSubStr.equals("true")) {
                throw new Exception();
            }
        } else if (dto.getRole().equals("ROLE_PROFESSOR")) {
            ProfessorEntity entity = PROFESSOR_REP.findById(dto.getIuser()).get();
            String email = entity.getEmail();
            String emailSubStr = email.substring(email.length()-4, email.length());

            if (!emailSubStr.equals("true")){
                throw new Exception();
            }
        }
        return true;
    }

}
