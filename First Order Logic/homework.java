import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
public class homework {
    public static void main(String args[]) throws FileNotFoundException {

        FileInputStream in = new FileInputStream("src/input-7.txt");
        Scanner scanner = new Scanner(in);
        int total_queries = Integer.parseInt(scanner.nextLine());
        String[] queries = new String[total_queries];
        for(int i = 0; i < total_queries; i++){
            queries[i] = scanner.nextLine();
        }
        int total_kb = Integer.parseInt(scanner.nextLine());
        String[] kbs = new String[total_kb];
        for(int i = 0; i < total_kb; i++){
            kbs[i] = scanner.nextLine();
        }

        //"Take(X,Warfarin) => ~Take(X,NSAIDs)", "HighBP(Y) => Alert(B,Kev)", "Take(Bob,Antacids)", "Take(Bob,VitA)"
        //Alert(Amy,Joe)
        FOL fol = new FOL();
        //fol.storeToKB(new String[]{"Migraine(x) & HighBP(x) => Take(x,Timolol)", "Take(x,Warfarin) & Take(x,Timolol) => Alert(x,VitE)", "Migraine(Alice)", "Migraine(Bob)", "HighBP(Bob)", "OldAge(John)", "HighBP(John)", "Take(John,Timolol)", "Take(Bob,Warfarin)"});
        //fol.negateQueryToKB("Alert(Bob,VitE)");
        //System.out.println(fol.proof());
        List<Boolean> result = new ArrayList<>();
        fol.storeToKB(kbs);
        for(int i = 0; i < queries.length; i++){
            fol.negateQueryToKB(queries[i]);
            result.add(fol.proof());

        }

        File out=new File("src/output.txt");
        try{

            FileWriter fwriter=new FileWriter(out);
            for(int i = 0; i < result.size()-1; i++){
                if (result.get(i) == true){
                    fwriter.write("TRUE\n");
                }
                else{
                    fwriter.write("FALSE\n");
                }
                System.out.println(result.get(i));
            }
            if (result.get(result.size()-1) == true){
                fwriter.write("TRUE\n");
            }
            else{
                fwriter.write("FALSE\n");
            }
            System.out.println(result.get(result.size()-1));
            fwriter.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}