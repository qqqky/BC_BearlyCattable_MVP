package app;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * Class for dynamic retrieval of deployment path
 */
public class PathRetriever {

	
/*
 * Dynamically retrieves the current deployment path of the DB.
 * Example of usage: retrievePath(System.getProperty("catalina.base"), "reg.db")
 */
public static final List<Path> retrievePath(String base, String dbName){
		
		base = base+System.getProperty("file.separator");
		List<Path> list = null;
		
		System.out.println("PathRetriever will execute search starting with this path: "+System.getProperty("line.separator")+base);
		try {
			Stream<Path> str = Files.find(Paths.get(base), 10, (path, attr) -> path.getFileName().toString().contains(dbName), FileVisitOption.FOLLOW_LINKS);
			list = str.collect(Collectors.toList());
		}catch(IOException e) {
			System.err.println("Error obtaining path to the database in PathRetriever");
		}
	
		return list==null? new ArrayList<>() : new ArrayList<>(list);
		
	}
}
