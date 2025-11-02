package class091;

import java.util.ArrayList;
import java.util.List;

// 划分字母区间
// 给你一个字符串 s 。我们要把这个字符串划分为尽可能多的片段，同一字母最多出现在一个片段中。
// 注意，划分结果需要满足：将所有划分结果按顺序连接，得到的字符串仍然是 s 。
// 返回一个表示每个字符串片段的长度的列表。
// 测试链接 : https://leetcode.cn/problems/partition-labels/
public class Code25_PartitionLabels {

    /**
     * 划分字母区间问题
     * 
     * 算法思路：
     * 使用贪心策略：
     * 1. 首先遍历字符串，记录每个字符最后一次出现的位置
     * 2. 再次遍历字符串，维护当前片段的结束位置end
     * 3. 对于当前字符，如果它的最后出现位置大于当前的end，则更新end
     * 4. 当遍历到i等于end时，说明找到了一个完整的片段，记录长度并开始新的片段
     * 
     * 正确性分析：
     * 1. 通过记录每个字符最后一次出现的位置，我们可以确定一个片段至少需要延伸到哪里
     * 2. 贪心地扩展end，确保同一字母只出现在一个片段中
     * 3. 当i到达end时，说明当前片段中的所有字符都不会出现在后面的片段中
     * 
     * 时间复杂度：O(n) - 其中n是字符串的长度，需要两次遍历字符串
     * 空间复杂度：O(1) - 只使用了常数级别的额外空间，因为字符集大小是固定的
     * 
     * @param s 输入字符串
     * @return 每个字符串片段的长度列表
     */
    public static List<Integer> partitionLabels(String s) {
        // 边界检查
        if (s == null || s.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 记录每个字符最后一次出现的位置
        int[] lastPosition = new int[26];
        for (int i = 0; i < s.length(); i++) {
            lastPosition[s.charAt(i) - 'a'] = i;
        }
        
        List<Integer> result = new ArrayList<>();
        int start = 0;  // 当前片段的起始位置
        int end = 0;    // 当前片段的结束位置
        
        // 遍历字符串，划分片段
        for (int i = 0; i < s.length(); i++) {
            // 更新当前片段的结束位置为当前字符的最后出现位置
            end = Math.max(end, lastPosition[s.charAt(i) - 'a']);
            
            // 当遍历到当前片段的结束位置时，划分出一个片段
            if (i == end) {
                result.add(end - start + 1);  // 添加片段长度
                start = end + 1;  // 开始新的片段
            }
        }
        
        return result;
    }

    // 打印列表辅助函数
    public static void printList(List<Integer> list) {
        System.out.print("[");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
            if (i < list.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: s = "ababcbacadefegdehijhklij" -> 输出: [9,7,8]
        // 划分结果: "ababcbaca", "defegde", "hijhklij"
        String s1 = "ababcbacadefegdehijhklij";
        System.out.println("测试用例1:");
        System.out.println("输入字符串: " + s1);
        System.out.print("划分结果: ");
        printList(partitionLabels(s1));  // 期望输出: [9, 7, 8]
        
        // 测试用例2: s = "eccbbbbdec" -> 输出: [10]
        // 所有字符都在一个片段中
        String s2 = "eccbbbbdec";
        System.out.println("\n测试用例2:");
        System.out.println("输入字符串: " + s2);
        System.out.print("划分结果: ");
        printList(partitionLabels(s2));  // 期望输出: [10]
        
        // 测试用例3: s = "a" -> 输出: [1]
        String s3 = "a";
        System.out.println("\n测试用例3:");
        System.out.println("输入字符串: " + s3);
        System.out.print("划分结果: ");
        printList(partitionLabels(s3));  // 期望输出: [1]
    }
}