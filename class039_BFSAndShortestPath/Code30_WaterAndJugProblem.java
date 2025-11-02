package class062;

import java.util.*;

// 水壶问题
// 有两个容量分别为 x 升 和 y 升 的水壶以及无限多的水。
// 请判断能否通过使用这两个水壶，从而可以得到恰好 z 升 的水？
// 如果可以，最后请用以上水壶中的一或两个来盛放取得的 z 升 水。
// 你允许：
// 装满任意一个水壶
// 清空任意一个水壶
// 从一个水壶向另外一个水壶倒水，直到装满或者倒空
// 测试链接 : https://leetcode.cn/problems/water-and-jug-problem/
// 
// 算法思路：
// 使用BFS搜索所有可能的状态。每个状态用(a, b)表示，其中a是第一个水壶的水量，b是第二个水壶的水量。
// 通过六种操作生成新的状态：装满A、装满B、倒空A、倒空B、A倒向B、B倒向A。
// 
// 时间复杂度：O(x * y)，状态空间为x*y
// 空间复杂度：O(x * y)，用于存储访问状态
// 
// 工程化考量：
// 1. 状态表示：使用二维数组或哈希集合记录访问状态
// 2. 操作模拟：精确模拟六种倒水操作
// 3. 数学优化：使用贝祖定理（裴蜀定理）进行数学判断
// 4. 边界情况：z为0，z大于x+y等特殊情况
public class Code30_WaterAndJugProblem {

    public static boolean canMeasureWater(int x, int y, int z) {
        // 边界情况处理
        if (z == 0) return true;
        if (z > x + y) return false;
        if (x == 0 && y == 0) return z == 0;
        
        // 使用数学方法判断：贝祖定理
        // z必须是x和y的最大公约数的倍数
        int gcd = gcd(x, y);
        return z % gcd == 0;
    }
    
    // BFS版本：用于理解和验证小规模数据
    public static boolean canMeasureWaterBFS(int x, int y, int z) {
        if (z == 0) return true;
        if (z > x + y) return false;
        if (x == 0 && y == 0) return z == 0;
        
        // 使用BFS搜索所有可能状态
        Queue<int[]> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        // 初始状态：两个水壶都为空
        queue.offer(new int[]{0, 0});
        visited.add(hash(0, 0));
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int a = current[0];
            int b = current[1];
            
            // 检查是否达到目标
            if (a == z || b == z || a + b == z) {
                return true;
            }
            
            // 生成所有可能的操作
            List<int[]> nextStates = generateNextStates(a, b, x, y);
            
            for (int[] next : nextStates) {
                long hash = hash(next[0], next[1]);
                if (!visited.contains(hash)) {
                    visited.add(hash);
                    queue.offer(next);
                }
            }
        }
        
        return false;
    }
    
    // 生成所有可能的下一状态
    private static List<int[]> generateNextStates(int a, int b, int x, int y) {
        List<int[]> states = new ArrayList<>();
        
        // 1. 装满A
        states.add(new int[]{x, b});
        
        // 2. 装满B
        states.add(new int[]{a, y});
        
        // 3. 倒空A
        states.add(new int[]{0, b});
        
        // 4. 倒空B
        states.add(new int[]{a, 0});
        
        // 5. A倒向B
        int pourAB = Math.min(a, y - b); // 可以倒出的水量
        states.add(new int[]{a - pourAB, b + pourAB});
        
        // 6. B倒向A
        int pourBA = Math.min(b, x - a); // 可以倒出的水量
        states.add(new int[]{a + pourBA, b - pourBA});
        
        return states;
    }
    
    // 哈希函数：将二维状态映射为一维
    private static long hash(int a, int b) {
        return (long) a * 1000000 + b;
    }
    
    // 计算最大公约数（欧几里得算法）
    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
    
    // 优化版本：使用数学方法 + BFS小规模验证
    public static boolean canMeasureWaterOptimized(int x, int y, int z) {
        // 数学方法快速判断
        if (z == 0) return true;
        if (z > x + y) return false;
        if (x == 0) return z == y || z == 0;
        if (y == 0) return z == x || z == 0;
        
        int gcd = gcd(x, y);
        if (z % gcd != 0) return false;
        
        // 对于小规模数据，使用BFS验证
        if (x * y <= 1000000) {
            return canMeasureWaterBFS(x, y, z);
        }
        
        return true;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        System.out.println("测试用例1 - 是否可以测量: " + canMeasureWater(3, 5, 4)); // 期望输出: true
        
        // 测试用例2：无法测量
        System.out.println("测试用例2 - 是否可以测量: " + canMeasureWater(2, 6, 5)); // 期望输出: false
        
        // 测试用例3：边界情况
        System.out.println("测试用例3 - 是否可以测量: " + canMeasureWater(0, 0, 0)); // 期望输出: true
        
        // 测试用例4：大数测试
        System.out.println("测试用例4 - 是否可以测量: " + canMeasureWater(104659, 104677, 142528)); // 期望输出: true
        
        // 测试用例5：贝祖定理验证
        System.out.println("测试用例5 - 是否可以测量: " + canMeasureWater(4, 6, 8)); // 期望输出: true
    }
}