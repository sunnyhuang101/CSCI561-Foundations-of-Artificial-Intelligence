import java.lang.reflect.Array;
import java.util.*;
public class FOL {

    Map<String, HashSet<String>> kb = new HashMap<>();
    Map<String, HashSet<String>> tmp_kb = new HashMap<>();
    HashMap<String, HashSet<String>> new_clauses_hash = new HashMap<>();
    HashSet<String> new_clauses_set = new HashSet<>();
    int standardize_count = 0;
    int tmp_standardize_count = 0;
    boolean same;
    int kb_size = 0;
    int resolutionCount = 0;
    Map<String, String> substitution = new HashMap<>();
    //x1, y1, "KKK"
    //"ABC", "OMG", "KKK"

    public Boolean predicateUnify(List<String> pred1_argus, List<String> pred2_argus){//input: lists of arguments
        substitution.clear();
        for(int i = 0; i < pred1_argus.size(); i++){
            String s1 = pred1_argus.get(i);
            String s2 = pred2_argus.get(i);
            if (Character.isUpperCase(s1.charAt(0)) == true && Character.isUpperCase(s2.charAt(0)) == true){
                if (s1.equals(s2) == false){
                    return false;
                }
            }
            else{
                if (unify(s1, s2) == false){
                    return false;
                }
            }
        }

        return true;
    }

    public Boolean unify(String argu1, String argu2){

        if (Character.isUpperCase(argu1.charAt(0)) == true && Character.isUpperCase(argu2.charAt(0)) == true && argu1.equals(argu2)){
            return true;
        }
        else if (Character.isLowerCase(argu1.charAt(0)) == true){
            return unifyVar(argu1, argu2);
        }
        else if (Character.isLowerCase(argu2.charAt(0)) == true){
            return unifyVar(argu2, argu1);
        }

        return false;

    }

    public Boolean unifyVar(String x, String y){ //x: variable, y: value
        if (substitution.containsKey(x) == true){
            return unify(substitution.get(x), y);
        }
        else if (substitution.containsKey(y)){
           return unify(x, substitution.get(y));
        }
        //else if (Character.isUpperCase(y.charAt(0)) == true) {// what if both x and y are variables and don't have pairs in substition
         else{
            substitution.put(x, y);
            return true;
            //return unify(y, y);
        }

        //return false;
    }



    public void storeToKB(String[] input_kbs){
        for(int i = 0; i < input_kbs.length; i++){
            Boolean has_pred = false;
            char[] clause_chars; //= input_kbs[i].toCharArray();
            int cur = 0;
            boolean var_exist = false;
            StringBuilder clausebuilder = new StringBuilder();
            ArrayList<String> keys = new ArrayList<>();
            StringBuilder predicatebuilder = new StringBuilder();

            char[] tmp = input_kbs[i].toCharArray();
            List<Character> toHornArrayList = new ArrayList<>();

            for(int j = 0; j < tmp.length; j++){
                toHornArrayList.add(tmp[j]);
            }


            if (toHornArrayList.contains('=') ==true){

                String toHorn;
                int idx = toHornArrayList.indexOf('=');
                String premise = input_kbs[i].substring(0, idx-1);
                premise = premise.replaceAll("&", "|");
                //System.out.println(premise);
                char[] premise_chars = premise.toCharArray();
                StringBuilder premisebuilder = new StringBuilder();

                int p = 0;
                char pc;

                while(p < premise_chars.length){
                    pc = premise_chars[p];

                    if (pc == ' ') {
                        premisebuilder.append(pc);
                        p++;
                        continue;
                    }

                    if (pc == '|'){
                        premisebuilder.append(pc);
                        p++;
                        continue;
                    }

                    if (pc == '~'){
                        p++;
                        pc = premise_chars[p];

                        while(pc != ')'){
                            premisebuilder.append(pc);
                            p++;
                            pc = premise_chars[p];
                        }
                        premisebuilder.append(pc);
                        p++;

                    }
                    else{
                        premisebuilder.append('~');
                        premisebuilder.append(pc);
                        p++;
                        pc = premise_chars[p];
                        while(pc != ')'){
                            premisebuilder.append(pc);
                            p++;
                            pc = premise_chars[p];
                        }
                        premisebuilder.append(pc);
                        p++;
                    }
                }
                premisebuilder.append(input_kbs[i].substring(idx-1).replaceAll("=>", "|"));
                toHorn = premisebuilder.toString();
                clause_chars = toHorn.toCharArray();
                //System.out.println(toHorn);

            }
            else{
                clause_chars = input_kbs[i].toCharArray();
            }

            while(cur < clause_chars.length) {
                char c = clause_chars[cur];

                if (c == ' ') {
                    clausebuilder.append(c);
                    cur++;
                    continue;
                }

                if (c == '|'){
                    clausebuilder.append(c);
                    cur++;
                    continue;
                }



                while (c != '(') {
                    predicatebuilder.append(c);
                    clausebuilder.append(c);
                    cur++;
                    c = clause_chars[cur];
                }


                String key = predicatebuilder.toString();
                //clausebuilder.append(Arrays.copyOfRange(clause_chars, 0, cur+1)); //可以這樣合併嗎

                clausebuilder.append(c);
               cur++;
               c = clause_chars[cur];

               while(c!= ')') {
                   if (Character.isLowerCase(c)) {
                       clausebuilder.append(c);
                       clausebuilder.append(standardize_count);
                       var_exist = true;
                       cur++;
                       c = clause_chars[cur];

                   } else {
                        while(c != ',' && c != ')'){
                            clausebuilder.append(c);
                            cur++;
                            c = clause_chars[cur];
                        }
                   }
                   if (c == ',') {
                       clausebuilder.append(c);
                       cur++;
                       c = clause_chars[cur];
                   }

               }

               clausebuilder.append(c);
                cur++;

                keys.add(key);

                predicatebuilder.setLength(0);



            }

            for(int j = 0; j < keys.size(); j++){
                String key = keys.get(j);
                HashSet<String> set = new HashSet<>();
                if (kb.containsKey(key) == true) {
                    set = kb.get(key);
                }
                set.add(clausebuilder.toString());
                kb.put(key, set);

            }
            if (var_exist == true)
            standardize_count++;

        }


        return;
    }

    public void negateQueryToKB(String query){
        same = false;
        tmp_kb.clear();
        resolutionCount = 0;
        tmp_standardize_count = standardize_count;
        for(Map.Entry<String, HashSet<String>> e : kb.entrySet()){
            tmp_kb.put(e.getKey(), e.getValue());
        }

        String negate_q;

        if (query.charAt(0) == '~'){
            negate_q = query.substring(1);
        }
        else{
            negate_q = "~" + query;
        }


        StringBuilder predicatebuilder = new StringBuilder();
        StringBuilder clausebuilder = new StringBuilder();

        int cur = 0;
        char[] negate_q_chars = negate_q.toCharArray();
        char c = negate_q_chars[cur];

        while(c != '('){
            predicatebuilder.append(c);
            clausebuilder.append(c);
            cur++;
            c = negate_q_chars[cur];
        }

        String key = predicatebuilder.toString();
        clausebuilder.append(c);
        cur++;
        c = negate_q_chars[cur];

        while(c != ')'){
          /*  if (Character.isLowerCase(c)) {
                clausebuilder.append(c);
                clausebuilder.append(standardize_count);
                standardize_count++;
                cur++;
                c = negate_q_chars[cur];

            } else {*/
            while(c != ',' && c != ')'){
                clausebuilder.append(c);
                cur++;
                c = negate_q_chars[cur];
            }
          //  }
            if (c == ',') {
                clausebuilder.append(c);
                cur++;
                c = negate_q_chars[cur];
            }

        }
        clausebuilder.append(c);

        HashSet<String> set = new HashSet<>();
        if (tmp_kb.containsKey(key) == true) {
            set = tmp_kb.get(key);
        }
        set.add(clausebuilder.toString());
        tmp_kb.put(key, set);

        HashSet<String> negate_q_set = new HashSet<>();
        negate_q_set.add(negate_q);
        new_clauses_hash.clear();
        new_clauses_hash.put(key, negate_q_set);

/*
        for(Map.Entry<String, HashSet<String>> e : tmp_kb.entrySet()){
            new_clauses_hash.put(e.getKey(), e.getValue());
            //System.out.println(e.getKey());
            //System.out.println(e.getValue());
        }

*/

    }

    public void updateNewClausesHash(String old_cl, String new_cl){


        HashSet<String> new_result = new HashSet<>();
        String new_check_str = new String(new_cl);

        /*
        HashSet<String> result = new HashSet<>();
        String check_str = new String(old_cl);
        while(check_str.contains("(") ==true){
            int idx = check_str.indexOf("(");
            result.add(check_str.substring(0,idx));
            idx = check_str.indexOf(")");

            if (check_str.length() - 1 > idx ){
                check_str = check_str.substring(idx+3);
            }
            else{
                break;
            }
        }
*/
        while(new_check_str.contains("(") ==true){
            int idx = new_check_str.indexOf("(");
            new_result.add(new_check_str.substring(0,idx));
            idx = new_check_str.indexOf(")");

            if (new_check_str.length() - 1 > idx ){
                new_check_str = new_check_str.substring(idx+4);
            }
            else{
                break;
            }
        }
        /*
        for(String pred : result){
            if (new_clauses_hash.containsKey(pred) == true){
                new_clauses_hash.get(pred).remove(old_cl);
                if (new_clauses_hash.get(pred).isEmpty() == true){
                    new_clauses_hash.remove(pred);
                }

            }


        }*/
        for(String pred : new_result){
            if (new_clauses_hash.containsKey(pred) == true){
                new_clauses_hash.get(pred).add(new_cl);

            }
            else{
                HashSet<String> tmp = new HashSet<>();
                tmp.add(new_cl);
                new_clauses_hash.put(pred, tmp);
            }

            if (tmp_kb.containsKey(pred) == true){
                tmp_kb.get(pred).add(new_cl);
            }
            else{
                HashSet<String> tmp = new HashSet<>();
                tmp.add(new_cl);
                tmp_kb.put(pred, tmp);
            }
        }

        return;

    }

    public boolean proof(){



       // boolean empty = false;

        while(same == false){
            //empty = resolution(new_clauses_hash);
            resolution(new_clauses_hash);
            resolutionCount++;
            if (new_clauses_hash.isEmpty() == true)
                return true;

            if (resolutionCount > 400)
                return false;
        }
        return false;
    }

    public List<String> performUnification(List<String> input_strings){ //cur_clause, negate_clause
        List<String> uni_result = new ArrayList<>();
        boolean tmpplus = false;
        for(int j = 0; j < 2; j ++) {
            char[] ptmp = input_strings.get(j).toCharArray();
            int i = 0;
            char c = ptmp[i];
            StringBuilder stringbuilder = new StringBuilder();

            while (i < ptmp.length) {//take(.,.,.)|take(...)|alert(...)
                if (c == ' ') {
                    stringbuilder.append(c);

                    i++;
                    c = ptmp[i];
                    continue;
                }

                if (c == '|') {
                    stringbuilder.append(c);

                    i++;
                    c = ptmp[i];
                    continue;
                }

                while (c != '(') {
                    stringbuilder.append(c);
                    i++;
                    c = ptmp[i];
                }
                stringbuilder.append(c);
                i++;
                c = ptmp[i];

                while (c != ')') {
                    StringBuilder argubuilder = new StringBuilder();
                    while (c != ',' && c != ')') {
                        argubuilder.append(c);
                        i++;
                        c = ptmp[i];
                    }

                    if (Character.isUpperCase(argubuilder.charAt(0)) == true) {
                        stringbuilder.append(argubuilder);
                    } else {
                        if (substitution.containsKey(argubuilder.toString()) == true) {
                            stringbuilder.append(substitution.get(argubuilder.toString()));
                        }
                        else{
                            stringbuilder.append(argubuilder.charAt(0));
                            stringbuilder.append(tmp_standardize_count);
                            tmpplus = true;
                        }
                    }
                    stringbuilder.append(c);
                    i++;
                    if (i < ptmp.length ){

                        c = ptmp[i];
                        if (c == ' '){
                            break;
                        }
                    }
                    else
                        break;

                }


            }
            uni_result.add(stringbuilder.toString());
        }

        if (tmpplus == true)
            tmp_standardize_count++;

        return uni_result;
    }

    public String clauseAfterUni(String input_str, int clauseIdx){
        String result_str = "";
        int cur_pred = 0;
        String tmp_input_str = new String(input_str);
        int idx = 0;

        while(cur_pred < clauseIdx){
            idx = tmp_input_str.indexOf("|");
            tmp_input_str = tmp_input_str.substring(idx+2);
            cur_pred++;
        }

        idx = tmp_input_str.indexOf(")");
        result_str = tmp_input_str.substring(0, idx+1);

        return result_str;
    }


    public void resolution(HashMap<String, HashSet<String>> input_clauses){
        HashMap<String, HashSet<String>> clauses = new HashMap<>();

        for(Map.Entry<String, HashSet<String>> e : input_clauses.entrySet()){
            clauses.put(e.getKey(), e.getValue());
            //System.out.println(e.getValue());
        }


        new_clauses_hash.clear();
        HashSet<String> checkedPre = new HashSet<>();//if this predicate is checked, skip it

        for(Map.Entry<String, HashSet<String>> e : clauses.entrySet()){//going through all predicates

            String negate_predicate;
            String cur_predicate = e.getKey(); //which predicate
            boolean cur_pos = true;

            if (checkedPre.contains(cur_predicate) == true){
                continue;
            }

            if (cur_predicate.charAt(0) == '~'){
                cur_pos = false;
                negate_predicate = cur_predicate.substring(1);
            }
            else{
                negate_predicate = "~" + cur_predicate;
            }

            if (tmp_kb.containsKey(negate_predicate) == true){

                //HashSet<String> tmp_cur_set = new HashSet<>(); //new_clauses_hash.get(cur_predicate);

                //System.out.println(tmp_cur_set.isEmpty());

                //HashSet<String> tmp_negate_set =  new HashSet<>();//new_clauses_hash.get(negate_predicate);
                //System.out.println(tmp_negate_set.isEmpty());

                for(String cur_clause : e.getValue()){//可能很長，但我們只要有predicate那段

                    boolean resolved = false;
                    int p_idx = cur_clause.indexOf(cur_predicate);

                    int predNum = 0;//此pred中是clause第幾個pred
                    int predIdx = p_idx;
                    String predClause = cur_clause.substring(0, predIdx);
                    while(predClause.contains(")")){
                        predNum++;
                        predIdx = predClause.indexOf("|");
                        if (predIdx < predClause.length()-2) {
                            predClause = predClause.substring(predIdx + 2);
                        }else{
                            break;
                        }
                    }

                    String tmp = cur_clause.substring(p_idx);
                    int b_idx = tmp.indexOf(")");
                    String check_cur_clause = tmp.substring(0, b_idx+1);
                    //System.out.println(check_cur_clause);
                    String check_n_clause;


                    String check_unify_clause = new String(check_cur_clause);
                    b_idx = check_unify_clause.indexOf("(");
                    check_unify_clause = check_unify_clause.substring(b_idx+1);
                    List<String> check_clause_arg = new ArrayList<>();

                    while(check_unify_clause.contains(")")) {
                        if (check_unify_clause.contains(",") == true) {
                            b_idx = check_unify_clause.indexOf(",");
                            check_clause_arg.add(check_unify_clause.substring(0, b_idx));
                            check_unify_clause = check_unify_clause.substring(b_idx + 1);
                        }
                        else{
                            b_idx = check_unify_clause.indexOf(")");
                            check_clause_arg.add(check_unify_clause.substring(0, b_idx));
                            check_unify_clause = "";
                        }
                    }

                     //for(int k = 0; k < check_clause_arg.size(); k++){
                    //System.out.println(check_clause_arg.get(k));
                    //}


                    //應該是unify之後
                    /*
                    if (cur_pos == true){
                        check_n_clause = "~" + check_cur_clause;
                    }
                    else{
                        check_n_clause = check_cur_clause.substring(1);
                    }
*/

                    HashSet<String> negate_clauses = tmp_kb.get(negate_predicate);
                    //System.out.println(negate_clauses);
                     for(String negate_clause : negate_clauses ){//go through all negate clauses

                         int np_idx = negate_clause.indexOf(negate_predicate);
                         //System.out.println(negate_predicate + " " + negate_clause);
                         String ntmp = negate_clause.substring(np_idx);
                         int nb_idx = ntmp.indexOf(")");
                         String check_n_unify_clause = ntmp.substring(0, nb_idx+1);
                         nb_idx = check_n_unify_clause.indexOf("(");
                         check_n_unify_clause = check_n_unify_clause.substring(nb_idx+1);
                         List<String> check_n_clause_arg = new ArrayList<>();

                         while(check_n_unify_clause.contains(")")) {
                             if (check_n_unify_clause.contains(",") == true) {
                                 nb_idx = check_n_unify_clause.indexOf(",");
                                 check_n_clause_arg.add(check_n_unify_clause.substring(0, nb_idx));
                                 check_n_unify_clause = check_n_unify_clause.substring(nb_idx + 1);
                             }
                             else{
                                 nb_idx = check_n_unify_clause.indexOf(")");
                                 check_n_clause_arg.add(check_n_unify_clause.substring(0, nb_idx));
                                 check_n_unify_clause = "";
                             }
                         }

                         Boolean canUnify = predicateUnify(check_clause_arg, check_n_clause_arg);

                        if (canUnify == true) {

                            int npredNum = 0;//此pred中是clause第幾個pred
                            int npredIdx = np_idx;
                            String npredClause = negate_clause.substring(0, npredIdx);
                            while(npredClause.contains(")")){
                                npredNum++;
                                //npredClause = npredClause.substring(npredIdx);
                                npredIdx = npredClause.indexOf("|");
                                if (npredIdx < npredClause.length()-2) {
                                    npredClause = npredClause.substring(npredIdx + 2);
                                }else{
                                    break;
                                }
                            }

                            List<String> sen = new ArrayList<>();
                            sen.add(cur_clause);
                            sen.add(negate_clause);
                            List<String> unified = new ArrayList<>();
                            unified = performUnification(sen);

                            cur_clause = unified.get(0);
                            negate_clause = unified.get(1);
                        //}
                            check_cur_clause = clauseAfterUni(cur_clause, predNum);
                            check_n_clause = clauseAfterUni(negate_clause, npredNum);

                        //if (canUnify == true ){ //&& negate_clause.contains(check_n_clause) == true
                            resolved = true;
                            //System.out.println(negate_clause);
                            // Take() or Take() | ... or | Take or ... | Take | ...

                            int n_idx = negate_clause.indexOf(check_n_clause);
                            //Boolean cur_empty = false;
                            if (n_idx == 0 && negate_clause.length() == check_n_clause.length()){
                                //System.out.println("1");
                                //tmp_negate_set.add(check_n_clause);

                                int c_idx = cur_clause.indexOf(check_cur_clause);

                                if (c_idx == 0 && cur_clause.length() == check_cur_clause.length()){
                                    new_clauses_hash.clear();
                                    return;
                                    //System.out.println("yo");
                                //continue;

                                }

                                /*
                                if (tmp_kb.containsKey(negate_predicate) == true){
                                    tmp_kb.get(negate_predicate).add(negate_clause);
                                }
                                else{
                                    HashSet<String> tmp_c = new HashSet<>();
                                    tmp_c.add(negate_clause);
                                    new_clauses_hash.put(negate_predicate, tmp_c);
                                }
                                */
                                //updateNewClausesHash(negate_clause, check_n_clause);
                            }
                            else if (n_idx == 0 && negate_clause.charAt(check_n_clause.length()) == ' '){
                                //System.out.println("2");
                                updateNewClausesHash(negate_clause, negate_clause.substring(check_n_clause.length()+3));
                                //tmp_negate_set.add(negate_clause.substring(check_n_clause.length()+3));
                            }
                            else if((n_idx + check_n_clause.length()) == negate_clause.length()){
                                updateNewClausesHash(negate_clause,negate_clause.substring(0,n_idx-3));
                                //tmp_negate_set.add(negate_clause.substring(0,n_idx-3));
                                //System.out.println(negate_clause.substring(0,n_idx-3) + "----");
                            }
                            else{
                                //System.out.println("4");
                                updateNewClausesHash(negate_clause,negate_clause.substring(0,n_idx) + negate_clause.substring(n_idx+check_n_clause.length()+3));
                                //tmp_negate_set.add(negate_clause.substring(0,n_idx-3) + negate_clause.substring(n_idx+check_n_clause.length()+3));
                            }

                        }
                        /*
                        else{
                            //tmp_cur_set.add(cur_clause);
                            if (new_clauses_hash.containsKey(negate_predicate) == true){
                                new_clauses_hash.get(negate_predicate).add(negate_clause);
                            }
                            else{
                                HashSet<String> tmp_c = new HashSet<>();
                                tmp_c.add(negate_clause);
                                new_clauses_hash.put(negate_predicate, tmp_c);
                            }
                        }*/
                     }

                     if (resolved == true){
                         // Take() or Take() | ... or | Take or ... | Take | ...
                         int c_idx = cur_clause.indexOf(check_cur_clause);

                         if (c_idx == 0 && cur_clause.length() == check_cur_clause.length()){
                             //tmp_cur_set.add(check_cur_clause);
                             //updateNewClausesHash(cur_clause, check_cur_clause);
                            /*
                             if (new_clauses_hash.containsKey(cur_predicate) == true){
                                 new_clauses_hash.get(cur_predicate).add(cur_clause);
                             }
                             else{
                                 HashSet<String> tmp_c = new HashSet<>();
                                 tmp_c.add(cur_clause);
                                 new_clauses_hash.put(cur_predicate, tmp_c);
                             }
                             */
                            continue;

                         }


                        else if (c_idx == 0 && cur_clause.charAt(check_cur_clause.length()) == ' '){
                             //tmp_cur_set.add(cur_clause.substring(check_cur_clause.length()+3));

                             updateNewClausesHash(cur_clause, cur_clause.substring(check_cur_clause.length()+3));
                         }
                         else if((c_idx + check_cur_clause.length()) == cur_clause.length()){
                             //tmp_cur_set.add(cur_clause.substring(0,c_idx-3));
                             updateNewClausesHash(cur_clause, cur_clause.substring(0,c_idx-3));
                             //System.out.println(cur_clause.substring(0,c_idx-3) + "----");
                         }
                         else{
                             //tmp_cur_set.add(cur_clause.substring(0,c_idx-3) + cur_clause.substring(c_idx+check_cur_clause.length()+3));
                             updateNewClausesHash(cur_clause, cur_clause.substring(0,c_idx) + cur_clause.substring(c_idx+check_cur_clause.length()+3));
                         }

                     }
                     else if(resolved == false){
                         if (new_clauses_hash.containsKey(cur_predicate) == true){
                             new_clauses_hash.get(cur_predicate).add(cur_clause);
                         }
                         else{
                             HashSet<String> tmp_c = new HashSet<>();
                             tmp_c.add(cur_clause);
                             new_clauses_hash.put(cur_predicate, tmp_c);
                         }
                         //new_clauses_hash.put(cur_predicate, e.getValue());
                         //new_clauses_hash.put(negate_predicate, clauses.get(negate_predicate));
                     }
                }


                //new_clauses_hash.put(cur_predicate, tmp_cur_set);
                //new_clauses_hash.put(negate_predicate, tmp_negate_set);
                checkedPre.add(cur_predicate);
                checkedPre.add(negate_predicate);
            }
            else{
                new_clauses_hash.put(cur_predicate, e.getValue());
                checkedPre.add(cur_predicate);
            }
        }

        boolean now_same = true;

        for(Map.Entry<String, HashSet<String>> e : clauses.entrySet()) {
            if (new_clauses_hash.containsKey(e.getKey()) == false){
                now_same = false;
                break;
            }
            else {
                if (new_clauses_hash.get(e.getKey()).equals(e.getValue()) == false){
                    now_same = false;
                    break;
                }
            }
        }

        if (now_same == true){
            same = true;
        }


       return;

    }
}
