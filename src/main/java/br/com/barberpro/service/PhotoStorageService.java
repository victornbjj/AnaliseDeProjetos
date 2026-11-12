package br.com.barberpro.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoStorageService {
  private static final long MAX_BYTES = 2 * 1024 * 1024;
  private static final Map<String, String> EXTENSIONS =
      Map.of("image/jpeg", ".jpg", "image/png", ".png", "image/webp", ".webp");
  private final Path uploadDirectory;

  public PhotoStorageService(@Value("${app.upload-dir:./data/barber-photos}") String dir) {
    uploadDirectory = Path.of(dir).toAbsolutePath().normalize();
  }

  public String store(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) return null;
    if (file.getSize() > MAX_BYTES)
      throw new IllegalArgumentException("A foto deve ter no máximo 2 MB.");
    String extension = EXTENSIONS.get(file.getContentType());
    if (extension == null)
      throw new IllegalArgumentException("Envie uma imagem JPEG, PNG ou WEBP.");
    Files.createDirectories(uploadDirectory);
    String filename = UUID.randomUUID() + extension;
    Path destination = uploadDirectory.resolve(filename).normalize();
    if (!destination.getParent().equals(uploadDirectory))
      throw new IllegalArgumentException("Arquivo inválido.");
    file.transferTo(destination);
    return "/uploads/" + filename;
  }
}
