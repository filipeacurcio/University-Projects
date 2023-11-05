package pt.isec.pa.apoio_poe.model.fsm;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.HashMap;
/** Cria uma classe para gerir as imagens,
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class ImageManager {

    private static final HashMap<String, Image> images = new HashMap<>();
    /** Cria uma classe para gerir as imagens.
     */
    private ImageManager() { }
    /** Obtém uma imagem.
     * @param filename Nome do ficheiro.
     * @return Uma classe imagem.
     */
    public static Image getImage(String filename) {

        Image image = images.get(filename);

        if (image == null) {
            try (InputStream is = ImageManager.class.getResourceAsStream("images/" + filename)) {
                image = new Image(is);
                images.put(filename, image);
            } catch (Exception e) {
                return null;
            }
        }

        return image;
    }

}