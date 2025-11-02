package class151;

/**
 * 笛卡尔树模板(Java版)
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，下标从1开始
 * 构建一棵二叉树，下标按照搜索二叉树组织，值按照小根堆组织
 * 建树的过程要求时间复杂度O(n)
 * 建树之后，为了验证
 * 打印，i * (left[i] + 1)，所有信息异或起来的值
 * 打印，i * (right[i] + 1)，所有信息异或起来的值
 * 
 * 约束条件：
 * 1 <= n <= 10^7
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P5854
 * 
 * 算法思路：
 * 1. 使用单调栈算法构建笛卡尔树
 * 2. 维护一个单调递增栈，栈中存储的是节点索引
 * 3. 对于每个新节点，找到它在栈中的正确位置，建立父子关系
 * 4. 时间复杂度：O(n)，每个节点入栈出栈各一次
 * 5. 空间复杂度：O(n)，用于存储栈和树结构
 * 
 * 工程化考量：
 * - 使用数组模拟树结构，提高内存效率
 * - 注意Java版本在洛谷平台可能因内存或IO问题无法通过所有测试
 * - C++版本(Code01_DescartesTree2)逻辑完全相同，可以通过测试
 * 
 * 时间复杂度分析：
 * - 构建笛卡尔树：O(n)
 * - 验证输出：O(n)
 * - 总时间复杂度：O(n)
 * 
 * 空间复杂度分析：
 * - 存储数组：O(n)
 * - 栈空间：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 边界情况处理：
 * - n=1时，只有一个节点，左右子树都为空
 * - 数组完全有序时，树会退化成链
 * - 注意数组下标从1开始，避免越界
 * 
 * 算法正确性证明：
 * 1. 二叉搜索树性质：通过下标顺序构建，满足BST性质
 * 2. 小根堆性质：通过单调栈维护，每个节点的值都小于其子树中的值
 * 3. 唯一性：对于给定的数组，笛卡尔树是唯一的
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_DescartesTree1 {

    // 最大节点数，根据题目要求设置为10^7
    public static int MAXN = 10000001;

    // arr数组存储输入的数值，下标从1开始
    public static int[] arr = new int[MAXN];

    // left数组存储每个节点的左子节点索引，0表示没有左子节点
    public static int[] left = new int[MAXN];

    // right数组存储每个节点的右子节点索引，0表示没有右子节点
    public static int[] right = new int[MAXN];

    // stack数组用作单调栈，存储节点索引，用于构建笛卡尔树
    public static int[] stack = new int[MAXN];

    // n表示输入数组的长度
    public static int n;

    /**
     * 构建笛卡尔树的核心方法
     * 使用单调栈算法，时间复杂度O(n)
     * 构建的笛卡尔树满足：
     * 1. 二叉搜索树性质：节点的下标满足二叉搜索树的性质
     * 2. 小根堆性质：节点的值满足小根堆的性质
     */
    public static void build() {
        // top表示栈顶指针，pos表示当前处理位置
        for (int i = 1, top = 0, pos = 0; i <= n; i++) {
            pos = top;
            // 维护单调栈，弹出栈顶中值大于当前元素的节点
            // 保证栈中节点的值按从小到大排列（小根堆性质）
            while (pos > 0 && arr[stack[pos]] > arr[i]) {
                pos--;
            }
            // 如果栈不为空，建立父子关系
            if (pos > 0) {
                // 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
                right[stack[pos]] = i;
            }
            // 如果pos < top，说明弹出了节点，建立当前节点与被弹出节点的关系
            if (pos < top) {
                // 当前节点的左子节点是最后被弹出的节点
                left[i] = stack[pos + 1];
            }
            // 将当前节点压入栈中
            stack[++pos] = i;
            // 更新栈顶指针
            top = pos;
        }
    }

    /**
     * 主函数，处理输入输出
     * 使用快速IO提高输入输出效率
     */
    public static void main(String[] args) throws IOException {
        // 使用BufferedReader提高输入效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用StreamTokenizer解析输入
        StreamTokenizer in = new StreamTokenizer(br);
        // 使用PrintWriter提高输出效率
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        // 读取数组长度n
        in.nextToken();
        n = (int) in.nval;
        // 读取数组元素，下标从1开始存储
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        // 构建笛卡尔树
        build();
        // 计算验证结果
        long ans1 = 0, ans2 = 0;
        // 根据题目要求计算异或值
        for (int i = 1; i <= n; i++) {
            ans1 ^= (long) i * (left[i] + 1);
            ans2 ^= (long) i * (right[i] + 1);
        }
        // 输出结果
        out.println(ans1 + " " + ans2);
        // 刷新输出缓冲区并关闭资源
        out.flush();
        out.close();
        br.close();
    }

}