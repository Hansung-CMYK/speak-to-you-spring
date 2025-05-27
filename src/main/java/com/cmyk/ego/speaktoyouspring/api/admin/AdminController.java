package com.cmyk.ego.speaktoyouspring.api.admin;

import com.cmyk.ego.speaktoyouspring.config.CommonResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminDbProperties db;

    private static final String LOG_FILE_NAME = "db-backup-log.txt";

    /**
     * 관리자 기능: FCM 직접 발송
     */
    @Operation(summary = "[관리자]: FCM 직접 발송", description = "보낸 즉시 해당 사용자에게 메세지를 보낼 수 있다. (현재 보내는 데이터는 의미 없는 값(발송한 사용자의 id와 보낸 시각)", tags = {"관리자"})
    @GetMapping("/fcm/{userId}")
    public ResponseEntity create(@PathVariable String userId) throws FirebaseMessagingException {

        // The topic name can be optionally prefixed with "/topics/".

        // See documentation on defining a message payload.
        Message message = Message.builder().putData("user_id", userId).putData("send_at", String.valueOf(LocalDateTime.now())).setTopic(userId).build();

        // Send a message to the devices subscribed to the provided topic.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);


        return ResponseEntity.ok(CommonResponse.builder().code(200).message("FCM 발송 완료").data(response).build());
    }

    @Operation(summary = "[관리자]: 리눅스용 DB 백업하는 API", description = "2개의 DB 백업을 수행한다. <br>기존의 백업 데이터에 덮어씌운다. (리눅스를 실행하려면, postgresql를 설치해야 한다.)", tags = {"관리자"})
    @PostMapping("/backup")
    public ResponseEntity backupLinux() {
        var result = handle("backup", db.getLinuxBackupDir());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("DB 백업 완료 (리눅스)").data(result).build());
    }

    @Operation(summary = "[관리자]: 윈도우용 DB 백업하는 API", description = "2개의 DB 백업을 수행한다. <br>기존의 백업 데이터에 덮어씌운다.", tags = {"관리자"})
    @PostMapping("/backup/windows")
    public ResponseEntity backupWindows() {
        var result = handle("backup", db.getWindowsBackupDir());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("DB 백업 완료 (윈도우)").data(result).build());
    }

    @Operation(summary = "[관리자]: 윈도우용 DB 복구하는 API", description = "2개의 DB 복구를 수행한다. <br>백업 API를 호출한 시점으로 DB 상태를 되돌린다. <h2>해당 API는 백업을 1번 반드시 하고 호출해야 한다.</h2> (리눅스를 실행하려면, postgresql를 설치해야 한다.)", tags = {"관리자"})
    @PostMapping("/restore")
    public ResponseEntity restoreLinux() {
        var result = handle("restore", db.getLinuxBackupDir());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("DB 복구 완료 (리눅스)").data(result).build());
    }

    @Operation(summary = "[관리자]: 윈도우용 DB 복구하는 API", description = "2개의 DB 복구를 수행한다. <br>백업 API를 호출한 시점으로 DB 상태를 되돌린다. <h2>해당 API는 백업을 1번 반드시 하고 호출해야 한다.</h2>", tags = {"관리자"})
    @PostMapping("/restore/windows")
    public ResponseEntity restoreWindows() {
        var result = handle("restore", db.getWindowsBackupDir());
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("DB 복구 완료 (윈도우)").data(result).build());
    }

    private String handle(String mode, String backupDirPath) {
        try {
            new File(backupDirPath).mkdirs();
            String log = logHeader(mode);

            log += exec(mode, db.getDbA(), backupDirPath + File.separator + "hub.dump");
            log += exec(mode, db.getDbB(), backupDirPath + File.separator + "personalized.dump");

            writeLog(backupDirPath + File.separator + LOG_FILE_NAME, log);
            return mode + " 성공\n" + log;
        } catch (Exception e) {
            String err = mode + " 실패: " + e.getMessage();
            writeLog(backupDirPath + File.separator + LOG_FILE_NAME, err);
            return err;
        }
    }

    private String exec(String mode, String dbName, String dumpPath) throws IOException, InterruptedException {
        String[] command;
        String os = System.getProperty("os.name").toLowerCase();
        String toolDir = os.contains("win") ? db.getWindowsPostgresPath() : "";

        if (mode.equals("backup")) {
            command = new String[]{
                    toolDir + (os.contains("win") ? "pg_dump.exe" : "pg_dump"),
                    "-U", db.getUser(), "-h", db.getHost(), "-p", db.getPort(), "-F", "c", "-f", dumpPath, dbName
            };
        } else {
            command = new String[]{
                    toolDir + (os.contains("win") ? "pg_restore.exe" : "pg_restore"),
                    "-U", db.getUser(), "-h", db.getHost(), "-p", db.getPort(), "-d", dbName, "-c", dumpPath
            };
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.environment().put("PGPASSWORD", db.getPassword());
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output = readStream(process.getInputStream());
        int exitCode = process.waitFor();

        return String.format("[%s %s] → 종료코드: %d\n%s\n",
                mode.toUpperCase(), dbName, exitCode, output);
    }

    private String readStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) result.append(line).append("\n");
        return result.toString();
    }

    private String logHeader(String type) {
        return "\n\n[" + type.toUpperCase() + "] " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n";
    }

    private void writeLog(String path, String content) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(content);
        } catch (IOException e) {
            System.err.println("로그 저장 실패: " + e.getMessage());
        }
    }
}
