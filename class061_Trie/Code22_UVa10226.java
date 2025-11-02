package class045_Trie;

import java.util.*;

/**
 * UVa 10226 Hardwood Species
 * 
 * 题目描述：
 * 统计森林中各种硬木的数量百分比。输入一系列树名，输出每种树名及其占总数的百分比。
 * 
 * 解题思路：
 * 1. 使用HashMap统计每种树的数量（虽然题目在Trie专题中，但此题更适合用HashMap）
 * 2. 遍历所有树名，统计每种树的数量
 * 3. 计算每种树的百分比并按字典序输出
 * 
 * 时间复杂度：O(N*logN)，其中N是树的数量（主要是排序的时间复杂度）
 * 空间复杂度：O(K)，其中K是不同树的种类数
 */
public class Code22_UVa10226 {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int t = Integer.parseInt(scanner.nextLine().trim()); // 测试用例数量
        scanner.nextLine(); // 消费空行
        
        for (int i = 0; i < t; i++) {
            if (i > 0) {
                System.out.println(); // 每个测试用例之间输出空行
            }
            
            Map<String, Integer> treeCount = new HashMap<>(); // 统计每种树的数量
            int totalCount = 0; // 树的总数量
            
            String line;
            // 读取树名，直到遇到空行或文件结束
            while (scanner.hasNextLine() && !(line = scanner.nextLine()).trim().isEmpty()) {
                String treeName = line.trim();
                treeCount.put(treeName, treeCount.getOrDefault(treeName, 0) + 1);
                totalCount++;
            }
            
            // 按字典序排序
            List<String> treeNames = new ArrayList<>(treeCount.keySet());
            Collections.sort(treeNames);
            
            // 输出每种树的百分比
            for (String treeName : treeNames) {
                int count = treeCount.get(treeName);
                double percentage = (double) count / totalCount * 100;
                System.out.printf("%s %.4f%n", treeName, percentage);
            }
        }
        
        scanner.close();
    }
}