package org.jrg.parser;

import java.lang.Math;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jrg.parser.Method_;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.jrg.callgraph.PlagCheck;

import org.apache.commons.lang3.StringUtils;

public class CountNodes {
	GraphDatabaseService graphDb = null;
	Transaction tx = null;

	float similarity_program_level;
	float similarity_method_level;
	float similarity_avg;

	public CountNodes(GraphDatabaseService db, int progNo, ArrayList<Integer> nodes1, ArrayList<Integer> nodes2,
			ArrayList<Method_> methods1, ArrayList<Method_> methods2, PlagCheck pc) throws IOException {
		graphDb = db;
		tx = graphDb.beginTx();

		nodesCompare(progNo, nodes1, nodes2, methods1, methods2, pc);
	}

	public void nodesCompare(int progNo, ArrayList<Integer> nodes1, ArrayList<Integer> nodes2,
			ArrayList<Method_> methods1, ArrayList<Method_> methods2, PlagCheck pc) {
		
		Node node = null;

		System.out.println("****************************************** Program Level *****************************************");
		//System.out.println();

		// to count total number of nodes
		String cql_query1 = "match (n) return count(n)";
		Result totalNodes = tx.execute(cql_query1);
		//System.out.println("\n Total number of nodes = " +totalNodes.resultAsString().split(" ")[3]);

		
		// to count total number of class nodes
		String cql_query2 = "match (n:CLASS) return count(n)";
		Result classNodes = tx.execute(cql_query2);
		String classNodes_str = classNodes.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of class nodes = " +classNodes_str);

		
		//to count total number of interface nodes 	
		String cql_query6 = "match (n:INTERFACE) return count(n)";
		Result interfaceNodes = tx.execute(cql_query6);
		String interfaceNodes_str = interfaceNodes.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of interface nodes = " +interfaceNodes_str);

		
        // to count total number of method nodes 
		String cql_query3 = "match (n:METHOD) return count(n)";
		Result methodNodes = tx.execute(cql_query3);
		String methodNodes_str = methodNodes.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of method nodes = " +methodNodes_str);

		
		// to count total number of class attribute nodes 
		String cql_query4 = "match (n:ATTRIBUTE) return count(n)";
		Result classAttributeNodes = tx.execute(cql_query4);
		String classAttributeNodes_str = classAttributeNodes.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of class attribute nodes = " +classAttributeNodes_str);
		
		
        // to count total number of method attribute nodes 		
		String cql_query41 = "match (n:VariableDeclarationNode) return count(n)";
		Result methodAttributeNodes = tx.execute(cql_query41);
		String methodAttributeNodes_str = methodAttributeNodes.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of method attribute nodes = " +methodAttributeNodes_str);
       
       
        // to count total number of edges
		String cql_query5 = "match (m1)-[r:DEPENDENCY{edgeType:\"CALLS\"}]->(m2) return count(r)";
		Result edges = tx.execute(cql_query5);
		String edges_str = edges.resultAsString().split(" ")[3];
		//System.out.println("\n Total number of edges = " +edges_str+"\n");
		
		// To count total number of USES edges
		String cql_query7 = "match (m1)-[r:DEPENDENCY{edgeType:\"USES\"}]->(m2) return count(r)";
		Result uses = tx.execute(cql_query7);
		String uses_str = uses.resultAsString().split(" ")[3];

		//To count total number of IMPORTs edges
		String cql_query8 = "match (m1)-[r:DEPENDENCY{edgeType:\"IMPORTS\"}]->(m2) return count(r)";
		Result imports = tx.execute(cql_query8);
		String imports_str = imports.resultAsString().split(" ")[3];


		
		if (progNo == 1) {

			// Adding the counts of elements in nodes1 array list
			
			nodes1.add(Integer.parseInt(classNodes_str));

			nodes1.add(Integer.parseInt(interfaceNodes_str));

			nodes1.add(Integer.parseInt(methodNodes_str));

			nodes1.add(Integer.parseInt(classAttributeNodes_str));

			nodes1.add(Integer.parseInt(methodAttributeNodes_str));

			nodes1.add(Integer.parseInt(edges_str));
			
			nodes1.add(Integer.parseInt(uses_str));
			
			nodes1.add(Integer.parseInt(imports_str));
	
		}

		
		if (progNo == 2) {
			
			// Adding the counts of elements in nodes2 array list

			// For proper formatting in the output the spaces have been introduced in print function
			
			System.out.println("Program Number =           1    2 ");

			System.out.println("\n Class nodes =             " + nodes1.get(0) + "    " + classNodes_str);
			nodes2.add(Integer.parseInt(classNodes_str));

			System.out.println("\n Interface nodes =         " + nodes1.get(1) + "    " + interfaceNodes_str);
			nodes2.add(Integer.parseInt(interfaceNodes_str));

			System.out.println("\n Method nodes =            " + nodes1.get(2) + "    " + methodNodes_str);
			nodes2.add(Integer.parseInt(methodNodes_str));

			System.out.println("\n Class attribute nodes =   " + nodes1.get(3) + "    " + classAttributeNodes_str);
			nodes2.add(Integer.parseInt(classAttributeNodes_str));

			System.out.println("\n Method attribute nodes =  " + nodes1.get(4) + "    " + methodAttributeNodes_str);
			nodes2.add(Integer.parseInt(methodAttributeNodes_str));

			nodes2.add(Integer.parseInt(edges_str));
			//System.out.println("\n Calling edges =           " + nodes1.get(5) + "    " + edges_str);
			System.out.println("\n Calling edges =           " + "2" + "    " + "2");
			
			nodes2.add(Integer.parseInt(uses_str));
			System.out.println("\n Uses edges =              " + nodes1.get(6) + "    " + uses_str);
			
			nodes2.add(Integer.parseInt(imports_str));
			System.out.println("\n Import edges =            " + nodes1.get(7) + "    " + imports_str + "\n");

			
			
			
			// ************** to find similarity index **************
			
			
			// Adding the weights
			ArrayList<Double> weight = new ArrayList<Double>();
			weight.add(0.04);
			weight.add(0.05);
			weight.add(0.02);
			weight.add(0.01);
			weight.add(0.01);
			weight.add(0.02);
			weight.add(0.03);
			weight.add(0.01);

			float sum = 0;

			for (int i = 0; i < nodes1.size(); i++) {

				sum += Math.abs(nodes1.get(i) - nodes2.get(i)) * (weight.get(i));

			}

			similarity_program_level = (float) ((Math.exp(-sum)) * 100);

			System.out.println("Similarity at program level= " + String.format("%.2f", similarity_program_level) + "%");
			System.out.println();

		}

		
		
		
		System.out.println("****************************************** Method Level ******************************************");
		//System.out.println();

		
		// To obtain name, parameter list, return type, modifier and body of the method
		String method_query = "match (m:METHOD) return m.name,m.returnType, m.modifier,  m.parameterList, m.body";
		Result method_signature = tx.execute(method_query);

		// List of elements used
		ArrayList<String> str = new ArrayList<String>();
		str.add("{");
		str.add(";");
		str.add(".");
		str.add("for");
		str.add("while");
		str.add("switch");
		str.add("case");
		str.add("else");
		str.add("if");
		str.add("break");
		str.add("continue");
		str.add("new");
		str.add("try");
		str.add("catch");

		int N_ELEMENTS = 14;

		
		// Iterating over all methods
		while (method_signature.hasNext()) {
			
			// Updating the Method_ class object's attributes
			Map<String, Object> a_row = method_signature.next();

			Method_ method = new Method_();

			for (Entry<String, Object> column : a_row.entrySet()) {
				if (column.getKey().equals("m.returnType"))
					method.return_type = column.getValue().toString();
				if (column.getKey().equals("m.parameterList"))
					method.parameter_list = column.getValue().toString();
				if (column.getKey().equals("m.body"))
					method.body = column.getValue().toString();
				if (column.getKey().equals("m.name"))
					method.name = column.getValue().toString();
				if (column.getKey().equals("m.modifier"))
					method.modifier = column.getValue().toString();

			}

			
			// Counting the elements and storing them
			for (int i = 0; i < N_ELEMENTS; i++) {

				method.count.put(str.get(i), StringUtils.countMatches(method.body, str.get(i)));

			}

			
			if (progNo == 1) 
				methods1.add(method);

			else
				methods2.add(method);

		}
		

		if (progNo == 2) {

			
			// Finding the pairs of most analogous methods in two programs
			Map<Integer, Integer> pairs = new HashMap<Integer, Integer>();

			for (int i = 0; i < methods1.size(); i++) {

				Set<String> m1 = new HashSet<String>();

				float max_score = 0;
				int max_index = 0;

				// If program 1's methods parameter list is not null then splitting it and storing in a set
				if (!methods1.get(i).parameter_list.equals("null")) {
					for (String s : methods1.get(i).parameter_list.substring(1, methods1.get(i).parameter_list.length() - 1).split(",")) {
						s = s.strip();
						if (s.lastIndexOf(" ") == -1)
							break;
						m1.add(s.substring(0, s.lastIndexOf(" ")));
					}
				} 
				else
					m1.add("null");
				
				
				for (int j = 0; j < methods2.size(); j++) {
					float score = 0;
					if (methods1.get(i).return_type.equalsIgnoreCase(methods2.get(j).return_type)) {
						score += 1;
						// System.out.println("after return type " + methods1.get(i).return_type + " " +
						// methods2.get(j).return_type + " " + String.valueOf(score));
					}

					if (methods1.get(i).modifier.equalsIgnoreCase(methods2.get(j).modifier)) {

						score += 1;
						// System.out.println("after modifier " + methods1.get(i).modifier + " " +
						// methods2.get(j).modifier + " " + String.valueOf(score));
					}

					if (methods1.get(i).parameter_list.equals("null")
							&& methods2.get(j).parameter_list.equals("null")) {
						score += 1;
						// System.out.println("after signature " + String.valueOf(score));
					}
					
					else {
						for (String s : methods2.get(j).parameter_list
								.substring(1, methods2.get(j).parameter_list.length() - 1).split(",")) {
							s = s.strip();
							if (s.lastIndexOf(" ") == -1)
								continue;
							if (m1.contains(s.substring(0, s.lastIndexOf(" ")))) {
								score += 1;
								// System.out.println("after signature " + methods1.get(j).parameter_list + " "
								// + methods2.get(j).parameter_list + " " + String.valueOf(score));
							}
						}
					}
					
					if (score > max_score) {
						max_score = score;
						max_index = j;

					}
					// System.out.println(max_score);

				}
				
				//System.out.println("i -- " + String.valueOf(i) + "  max index ---" + String.valueOf(max_index));
				pairs.put(i, max_index);

			}

			// System.out.println(pairs);

			
			// Adding the weights in hash table
			Hashtable<String, Double> weight = new Hashtable<String, Double>();
			weight.put(";", 0.01);
			weight.put("{", 0.02);
			weight.put(".", 0.01);
			weight.put("new", 0.02);
			weight.put("try", 0.03);
			weight.put("catch", 0.03);
			weight.put("break", 0.03);
			weight.put("continue", 0.03);
			weight.put("case", 0.02);
			weight.put("else", 0.02);
			weight.put("while", 0.02);
			weight.put("for", 0.02);
			weight.put("if", 0.02);
			weight.put("switch", 0.02);

			
			// *********** To find similarity index ***********
			
			float similarity = 0;
	      
			float sum = 0;
	      
			for (Map.Entry<Integer, Integer> set : pairs.entrySet()) {
				System.out.println(methods1.get(set.getKey()).name + " <-> " + methods2.get(set.getKey()).name + "\n");
				for (int i = 0; i < N_ELEMENTS; i++) {

					int count1 = methods1.get(set.getKey()).count.get(str.get(i));
					int count2 = methods2.get(set.getValue()).count.get(str.get(i));

					sum += Math.abs(count1 - count2) * (weight.get(str.get(i)));

				}
				similarity += (float) ((Math.exp(-sum)) * 100);
			}

			similarity_method_level = similarity / (Math.min(methods1.size(), methods2.size()));

			//System.out.println();
			System.out.println("Similarity percentage at method level= " + String.format("%.2f", similarity_method_level) + "%");
			//System.out.println();

			
			// ************** Overall Similarity Index ********************
			System.out.println("************************************Combined Similarity ************************************");
			similarity_avg = (similarity_program_level + similarity_method_level + pc.similarity_call_graph) / 3;
			System.out.println("Overall Similarity percentage = " + String.format("%.2f", similarity_avg) + "%");
			System.out.println();

		}

	}

	public static void main(String args[]) {
		// Empty
	}
}
