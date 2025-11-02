package class151;

// SPOJ ORDERSET - Order statistic set
// 维护一个可重集合，支持以下操作：
// 1. 插入元素
// 2. 删除元素
// 3. 查询元素排名
// 4. 查询第k小值
// 测试链接 : https://www.spoj.com/problems/ORDERSET/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class SPOJ_ORDERSET {

    // 最大节点数
    public static int MAXN = 200001;

    // 整棵树的头节点编号（根节点）
    public static int head = 0;

    // 空间使用计数，记录当前已分配的节点数量
    public static int cnt = 0;

    // 节点的key值（存储实际数值）
    public static int[] key = new int[MAXN];

    // 节点key的计数（词频压缩，相同值只存储一次但记录出现次数）
    public static int[] count = new int[MAXN];

    // 左孩子节点索引数组
    public static int[] left = new int[MAXN];

    // 右孩子节点索引数组
    public static int[] right = new int[MAXN];

    // 子树大小数组，记录以每个节点为根的子树中节点总数
    public static int[] size = new int[MAXN];

    // 节点优先级数组，用于维护Treap的堆性质
    public static double[] priority = new double[MAXN];

    /**
     * 更新节点信息
     * 计算以节点i为根的子树大小
     * @param i 节点索引
     */
    public static void up(int i) {
        // 子树大小 = 左子树大小 + 右子树大小 + 当前节点的词频
        size[i] = size[left[i]] + size[right[i]] + count[i];
    }

    /**
     * 左旋转
     * 当右子节点的优先级大于当前节点时执行
     * @param i 当前节点
     * @return 旋转后的新根节点
     */
    public static int leftRotate(int i) {
        // 获取右子节点作为新的根节点
        int r = right[i];
        // 将右子节点的左子树作为当前节点的右子树
        right[i] = left[r];
        // 将当前节点作为原右子节点的左子树
        left[r] = i;
        // 更新节点信息
        up(i);
        up(r);
        // 返回新的根节点
        return r;
    }

    /**
     * 右旋转
     * 当左子节点的优先级大于当前节点时执行
     * @param i 当前节点
     * @return 旋转后的新根节点
     */
    public static int rightRotate(int i) {
        // 获取左子节点作为新的根节点
        int l = left[i];
        // 将左子节点的右子树作为当前节点的左子树
        left[i] = right[l];
        // 将当前节点作为原左子节点的右子树
        right[l] = i;
        // 更新节点信息
        up(i);
        up(l);
        // 返回新的根节点
        return l;
    }

    /**
     * 添加节点的递归实现
     * @param i 当前节点索引
     * @param num 要插入的数值
     * @return 插入后的新节点索引
     */
    public static int add(int i, int num) {
        // 如果当前节点为空，创建新节点
        if (i == 0) {
            // 分配新节点
            key[++cnt] = num;
            // 初始化词频和子树大小
            count[cnt] = size[cnt] = 1;
            // 生成随机优先级
            priority[cnt] = Math.random();
            // 返回新节点索引
            return cnt;
        }
        // 如果要插入的值等于当前节点值，增加词频
        if (key[i] == num) {
            count[i]++;
        } 
        // 如果要插入的值小于当前节点值，递归插入到左子树
        else if (key[i] > num) {
            left[i] = add(left[i], num);
        } 
        // 如果要插入的值大于当前节点值，递归插入到右子树
        else {
            right[i] = add(right[i], num);
        }
        // 更新当前节点的子树大小信息
        up(i);
        // 检查是否需要旋转以维护堆性质
        // 如果左子节点优先级大于当前节点，执行右旋
        if (left[i] != 0 && priority[left[i]] > priority[i]) {
            return rightRotate(i);
        }
        // 如果右子节点优先级大于当前节点，执行左旋
        if (right[i] != 0 && priority[right[i]] > priority[i]) {
            return leftRotate(i);
        }
        // 不需要旋转，返回当前节点
        return i;
    }

    /**
     * 添加元素的公共接口
     * @param num 要添加的数值
     */
    public static void add(int num) {
        head = add(head, num);
    }

    /**
     * 计算小于num的元素个数
     * @param i 当前节点索引
     * @param num 目标数值
     * @return 小于num的元素个数
     */
    public static int small(int i, int num) {
        // 如果当前节点为空，返回0
        if (i == 0) {
            return 0;
        }
        // 如果当前节点值大于等于目标值，递归查询左子树
        if (key[i] >= num) {
            return small(left[i], num);
        } 
        // 如果当前节点值小于目标值，结果包括：
        // 1. 左子树的所有节点
        // 2. 当前节点的词频
        // 3. 右子树中小于num的节点数
        else {
            return size[left[i]] + count[i] + small(right[i], num);
        }
    }

    /**
     * 查询排名
     * @param num 目标数值
     * @return num的排名（比num小的数的个数+1）
     */
    public static int rank(int num) {
        return small(head, num) + 1;
    }

    /**
     * 查询排名为x的数
     * @param i 当前节点索引
     * @param x 排名
     * @return 排名为x的数值
     */
    public static int index(int i, int x) {
        // 如果左子树大小大于等于x，说明目标在左子树中
        if (size[left[i]] >= x) {
            return index(left[i], x);
        } 
        // 如果左子树大小加上当前节点词频小于x，说明目标在右子树中
        else if (size[left[i]] + count[i] < x) {
            return index(right[i], x - size[left[i]] - count[i]);
        }
        // 否则当前节点就是目标节点
        return key[i];
    }

    /**
     * 查询排名为x的数的公共接口
     * @param x 排名
     * @return 排名为x的数值
     */
    public static int index(int x) {
        // 检查排名是否合法
        if (x <= 0 || x > size[head]) {
            return Integer.MIN_VALUE;
        }
        return index(head, x);
    }

    /**
     * 查找前驱
     * @param i 当前节点索引
     * @param num 目标数值
     * @return x的前驱（小于x的最大数）
     */
    public static int pre(int i, int num) {
        // 如果当前节点为空，返回整数最小值
        if (i == 0) {
            return Integer.MIN_VALUE;
        }
        // 如果当前节点值大于等于目标值，递归查询左子树
        if (key[i] >= num) {
            return pre(left[i], num);
        } 
        // 如果当前节点值小于目标值，前驱可能是当前节点值或右子树中的最大值
        else {
            return Math.max(key[i], pre(right[i], num));
        }
    }

    /**
     * 查找前驱的公共接口
     * @param num 目标数值
     * @return x的前驱
     */
    public static int pre(int num) {
        return pre(head, num);
    }

    /**
     * 查找后继
     * @param i 当前节点索引
     * @param num 目标数值
     * @return x的后继（大于x的最小数）
     */
    public static int post(int i, int num) {
        // 如果当前节点为空，返回整数最大值
        if (i == 0) {
            return Integer.MAX_VALUE;
        }
        // 如果当前节点值小于等于目标值，递归查询右子树
        if (key[i] <= num) {
            return post(right[i], num);
        } 
        // 如果当前节点值大于目标值，后继可能是当前节点值或左子树中的最小值
        else {
            return Math.min(key[i], post(left[i], num));
        }
    }

    /**
     * 查找后继的公共接口
     * @param num 目标数值
     * @return x的后继
     */
    public static int post(int num) {
        return post(head, num);
    }

    /**
     * 删除节点的递归实现
     * @param i 当前节点索引
     * @param num 要删除的数值
     * @return 删除后的新节点索引
     */
    public static int remove(int i, int num) {
        // 如果当前节点为空，返回0
        if (i == 0) {
            return 0;
        }
        // 如果要删除的值小于当前节点值，递归删除左子树
        if (key[i] < num) {
            right[i] = remove(right[i], num);
        } 
        // 如果要删除的值大于当前节点值，递归删除右子树
        else if (key[i] > num) {
            left[i] = remove(left[i], num);
        } 
        // 如果要删除的值等于当前节点值
        else {
            // 如果词频大于1，只需减少词频
            if (count[i] > 1) {
                count[i]--;
            } 
            // 如果词频为1，需要真正删除节点
            else {
                // 如果是叶子节点，直接删除
                if (left[i] == 0 && right[i] == 0) {
                    return 0;
                } 
                // 如果只有左子树，用左子树替代当前节点
                else if (left[i] != 0 && right[i] == 0) {
                    i = left[i];
                } 
                // 如果只有右子树，用右子树替代当前节点
                else if (left[i] == 0 && right[i] != 0) {
                    i = right[i];
                } 
                // 如果左右子树都存在，根据优先级决定旋转方向
                else {
                    // 如果左子节点优先级更高，执行右旋
                    if (priority[left[i]] >= priority[right[i]]) {
                        i = rightRotate(i);
                        right[i] = remove(right[i], num);
                    } 
                    // 如果右子节点优先级更高，执行左旋
                    else {
                        i = leftRotate(i);
                        left[i] = remove(left[i], num);
                    }
                }
            }
        }
        // 更新节点信息
        up(i);
        // 返回当前节点
        return i;
    }

    /**
     * 删除元素的公共接口
     * @param num 要删除的数值
     */
    public static void remove(int num) {
        // 只有当num存在于树中时才执行删除操作
        if (rank(num) != rank(num + 1)) {
            head = remove(head, num);
        }
    }

    /**
     * 清空数据结构，重置所有数组
     */
    public static void clear() {
        Arrays.fill(key, 1, cnt + 1, 0);
        Arrays.fill(count, 1, cnt + 1, 0);
        Arrays.fill(left, 1, cnt + 1, 0);
        Arrays.fill(right, 1, cnt + 1, 0);
        Arrays.fill(size, 1, cnt + 1, 0);
        Arrays.fill(priority, 1, cnt + 1, 0);
        cnt = 0;
        head = 0;
    }

    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            // 读取整行操作指令
            String operation = br.readLine().trim();
            String[] parts = operation.split(" ");
            char op = parts[0].charAt(0);  // 操作类型
            int x = Integer.parseInt(parts[1]);  // 操作数
            
            if (op == 'I') {
                // 插入元素
                add(x);
            } else if (op == 'D') {
                // 删除元素
                remove(x);
            } else if (op == 'K') {
                // 查询第k小值
                int result = index(x);
                if (result == Integer.MIN_VALUE) {
                    out.println("invalid");
                } else {
                    out.println(result);
                }
            } else if (op == 'C') {
                // 查询排名（计算小于x的元素个数）
                out.println(small(head, x));
            }
        }
        clear();
        out.flush();
        out.close();
        br.close();
    }
}