package dad.knote;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication
public class KnoteJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnoteJavaApplication.class, args);
    }
}
@Repository
interface RepositorioTareas extends MongoRepository<Tarea, String> {
	@Query("{ '_id' : ?0 }")
	List<Tarea> findTareasByDescription(String description);
}

@Document(collection = "tareas")
class Tarea {
    public String id;
    public String description;

    public Tarea(String description) {
		this.description=description;
	}
    public String getId() {
        return id;
      }
    public String getDescription() {
        return description;
      }

}

@Configuration
@EnableConfigurationProperties(PropiedadesTarea.class)
class ConfiguracionTarea implements WebMvcConfigurer {

    @Autowired
    private PropiedadesTarea properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + properties.getUploadDir())
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}

@ConfigurationProperties(prefix = "knote")
class PropiedadesTarea {
    @Value("${uploadDir:/tmp/uploads/}")
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
}

@Controller
class TareaController {

    @Autowired
    private RepositorioTareas tareasRepository;
    @Autowired
    private PropiedadesTarea properties;

    private Parser parser = Parser.builder().build();
    private HtmlRenderer renderer = HtmlRenderer.builder().build();


    @GetMapping("/")
    public String index(Model model) {
    	getAllTareas(model);
        return "index";
    }

    @PostMapping("/tarea")
    public String guardarTareas(
                            @RequestParam String description,
                            Model model) throws Exception {

     
            guardarTarea(description, model);
            getAllTareas(model);
            return "redirect:/";
        
      
  
    }
    @PostMapping("/delete")
    public String eliminarTarea(
                            @RequestParam String tarea,
                            @RequestParam(required = false) String publish,
                            Model model) throws Exception {
    	
            eliminarTarea(tarea.trim(), model);
           getAllTareas(model);
            return "redirect:/";
        
      
  
    }
    


    private void getAllTareas(Model model) {
        List<Tarea> tareas = tareasRepository.findAll();
        Map<String, String> tareass = new HashMap<String, String>();
        Collections.reverse(tareas);
        for(int i=0; i<tareas.size();i++) {
        	tareass.put(tareas.get(i).getId(),tareas.get(i).getDescription() );
        	
        }
      
        model.addAttribute("tareas", tareass);
    }

    private void eliminarTarea(String tarea, Model model) {
   

    	tareasRepository.deleteById(tarea);
    }

    private void guardarTarea(String description, Model model) {
        if (description != null && !description.trim().isEmpty()) {
            //We need to translate markup to HTML
            Node document = parser.parse(description.trim());
            String html = renderer.render(document);
            tareasRepository.save(new Tarea( description.trim()));
            //After publish you need to clean up the textarea
            model.addAttribute("description", "");
        }
    }

}
