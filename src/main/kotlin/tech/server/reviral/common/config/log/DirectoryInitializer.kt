package tech.server.reviral.common.config.log

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *packageName    : tech.server.reviral.common.config.log
 * fileName       : DirectoryInitializer
 * author         : joy58
 * date           : 2025-01-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        joy58       최초 생성
 */
@Component
class DirectoryInitializer: CommandLineRunner {

    override fun run(vararg args: String?) {
        val os = System.getProperty("os.name").lowercase()

        val path: Path = when {
            os.contains("win") -> Paths.get("C:/Users/joy58/OneDrive/바탕 화면/log")
            os.contains("nix") || os.contains("nux") || os.contains("ubuntu") -> Paths.get("/home/ubuntu/reviral/log")
            else -> throw Exception("오류")
        }

        try {
            if ( !Files.exists(path) ) {
                Files.createDirectories(path)
                println("DIRECTORY CREATED ::::: $path")
            }else {
                println("DIRECTORY ALREADY EXIST ::: $path")
            }
        }catch ( e: Exception ) {
            println("DIRECTORY CREATED FAIL")
        }
    }
}