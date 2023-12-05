package emp_btlist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BT_LIST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3) {
            System.out.println("Usage: java OrganisationalChartTraversal <filename> <employee1> <employee2>");
            System.exit(1);
        }

        String filename = args[0];
        String employee1 = args[1];
        String employee2 = args[2];

        Map<String, Employee> employees = readEmployees(filename);
        Employee emp1 = findEmployeeByName(employees, employee1);
        Employee emp2 = findEmployeeByName(employees, employee2);

        if (emp1 != null && emp2 != null) {
            List<Employee> path = findShortestPath(emp1, emp2);
            printPath(path);
        } else {
            System.out.println("Employee not found.");
        }
    }

    private static Map<String, Employee> readEmployees(String filename) {
        Map<String, Employee> employees = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\\s*\\|\\s*");
                String name = values[2].trim();
                String managerId = values[3].trim();
                employees.put(values[2].trim(), new Employee(values[1].trim(), name, managerId));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employees;
    }

    private static Employee findEmployeeByName(Map<String, Employee> employees, String name) {
        for (Employee emp : employees.values()) {
            if (emp.getName().equalsIgnoreCase(name)) {
                return emp;
            }
        }
        return null;
    }

    private static List<Employee> findShortestPath(Employee source, Employee destination) {
        List<Employee> path = new ArrayList<>();
        Map<String, Boolean> visited = new HashMap<>();

        return findShortestPathDFS(source, destination, visited, path);
    }

    private static List<Employee> findShortestPathDFS(
            Employee current, Employee destination, Map<String, Boolean> visited, List<Employee> path) {
        visited.put(current.getName(), true);
        path.add(current);

        if (current.equals(destination)) {
            return path;
        }

        for (Employee subordinate : current.getSubordinates()) {
            if (!visited.getOrDefault(subordinate.getName(), false)) {
                List<Employee> newPath = findShortestPathDFS(subordinate, destination, visited, new ArrayList<>(path));
                if (newPath != null) {
                    return newPath;
                }
            }
        }

        return null;
    }

    private static void printPath(List<Employee> path) {
        if (path == null) {
            System.out.println("No path found.");
            return;
        }

        for (int i = 0; i < path.size(); i++) {
            Employee emp = path.get(i);
            System.out.print(emp.getName() + " (" + emp.getEmployeeId() + ")");
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}

class Employee {
    private String employeeId;
    private String name;
    private String managerId;
    private List<Employee> subordinates;

    public Employee(String employeeId, String name, String managerId) {
        this.employeeId = employeeId;
        this.name = name;
        this.managerId = managerId;
        this.subordinates = new ArrayList<>();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getManagerId() {
        return managerId;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void addSubordinate(Employee subordinate) {
        subordinates.add(subordinate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return name.equalsIgnoreCase(employee.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}

	}

}
