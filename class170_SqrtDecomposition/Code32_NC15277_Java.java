import java.util.*;

/**
 * 牛客网 NC15277 区间异或和
 * 题目要求：区间异或操作，单点查询
 * 核心技巧：分块标记 - 对完整的块进行标记，对不完整的块进行暴力修改
 * 时间复杂度：O(√n) / 操作
 * 空间复杂度：O(n)
 * 测试链接：https://ac.nowcoder.com/acm/problem/15277
 * 
 * 算法思想详解：
 * 1. 将数组分成大小为√n的块
 * 2. 对于区间异或操作：
 *    - 对于完全包含在区间内的块，更新块标记（lazy标记）
 *    - 对于不完整的块，暴力更新每个元素的值
 * 3. 对于单点查询：
 *    - 计算该元素所在块的标记异或上原始值
 *    - 返回最终结果
 * 
 * 这种方法利用了异或操作的性质：连续异或同一个数两次相当于没有异或
 * 分块处理使得每次操作的时间复杂度降为O(√n)，比暴力方法的O(n)更高效
 */
public class Code32_NC15277_Java {
    private int[] arr;      // 原始数组
    private int[] block;    // 每个块的异或标记（lazy标记）
    private int blockSize;  // 块的大小
    private int n;          // 数组长度
    
    /**
     * 构造函数，初始化数据结构
     * @param array 输入数组
     */
    public Code32_NC15277_Java(int[] array) {
        n = array.length;
        arr = Arrays.copyOf(array, n);
        blockSize = (int) Math.sqrt(n) + 1;
        block = new int[(n + blockSize - 1) / blockSize]; // 向上取整计算块数
    }
    
    /**
     * 区间异或操作
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @param val 异或的值
     */
    public void xorRange(int l, int r, int val) {
        // 处理左边界所在的不完整块
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        
        // 如果在同一个块内，直接暴力修改
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; i++) {
                arr[i] ^= val;
            }
            return;
        }
        
        // 处理左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
            arr[i] ^= val;
        }
        
        // 处理中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            block[i] ^= val;
        }
        
        // 处理右边不完整块
        for (int i = rightBlock * blockSize; i <= r; i++) {
            arr[i] ^= val;
        }
    }
    
    /**
     * 单点查询
     * @param index 查询的位置
     * @return 查询位置的最终值
     */
    public int query(int index) {
        int blockId = index / blockSize;
        return arr[index] ^ block[blockId];
    }
    
    /**
     * 获取完整的数组内容（考虑块标记的影响）
     * @return 处理后的完整数组
     */
    public int[] getArray() {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = query(i);
        }
        return result;
    }
    
    /**
     * 重置所有块标记
     * 这个方法可以用于优化，如果确定后续操作会覆盖整个数组，可以先重置
     */
    public void resetBlocks() {
        Arrays.fill(block, 0);
        // 如果需要，可以将块标记应用到原始数组，然后重置标记
        // 这里简化处理，仅重置标记数组
    }
    
    /**
     * 测试函数，演示基本功能
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入数组长度：");
        int n = scanner.nextInt();
        int[] array = new int[n];
        
        System.out.println("请输入数组元素：");
        for (int i = 0; i < n; i++) {
            array[i] = scanner.nextInt();
        }
        
        Code32_NC15277_Java solution = new Code32_NC15277_Java(array);
        
        System.out.println("请输入操作数量：");
        int q = scanner.nextInt();
        
        System.out.println("操作格式：1 l r val (区间异或) 或 2 index (单点查询)");
        while (q-- > 0) {
            int op = scanner.nextInt();
            if (op == 1) {
                // 区间异或操作
                int l = scanner.nextInt() - 1; // 转换为0-based索引
                int r = scanner.nextInt() - 1;
                int val = scanner.nextInt();
                solution.xorRange(l, r, val);
                System.out.println("区间异或操作完成");
            } else if (op == 2) {
                // 单点查询
                int index = scanner.nextInt() - 1; // 转换为0-based索引
                int result = solution.query(index);
                System.out.println("查询结果：" + result);
            }
        }
        
        scanner.close();
    }
    
    /**
     * 算法优化说明：
     * 1. 块大小的选择：通常选择√n是时间复杂度的平衡点
     * 2. 对于数据规模特别大的情况，可以考虑动态调整块大小
     * 3. 当操作次数非常多时，可以定期将块标记应用到原始数组，减少标记累积
     * 
     * 边界情况处理：
     * 1. 空数组：在构造函数中处理
     * 2. 非法索引：在实际应用中应添加越界检查
     * 3. 区间长度为1的情况：会正确处理为暴力修改
     * 
     * 时间复杂度分析：
     * - 区间操作：O(√n)
     * - 单点查询：O(1)
     * 
     * 空间复杂度分析：
     * - O(n) 用于存储原始数组和块标记
     */
}