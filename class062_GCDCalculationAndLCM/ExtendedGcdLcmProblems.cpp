#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <climits>
using namespace std;

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

/**
 * 扩展GCD和LCM相关问题的实现（C++版本）
 * 包含LeetCode和其他平台上的经典问题
 */

class ExtendedGcdLcmProblems {
public:
    /**
     * Codeforces 1034A. Enlarge GCD
     * 题目来源：https://codeforces.com/problemset/problem/1034/A
     * 问题描述：给定n个正整数，通过删除最少的数来增大这些数的最大公约数。
     *           返回需要删除的最少数字个数，如果无法增大GCD则返回-1。
     * 解题思路：首先计算所有数的GCD，然后将所有数除以这个GCD，问题转化为找到一个大于1的因子，
     *           使得尽可能多的数是这个因子的倍数。枚举所有质数，统计是其倍数的数的个数，
     *           答案就是n减去最大个数。
     * 时间复杂度：O(n*log(max_value) + max_value*log(log(max_value)))
     * 空间复杂度：O(max_value)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static int enlargeGCD(vector<int>& nums) {
        int n = nums.size();
        
        // 计算所有数的GCD
        int currentGcd = nums[0];
        for (int i = 1; i < n; i++) {
            currentGcd = gcd(currentGcd, nums[i]);
        }
        
        // 将所有数除以GCD
        vector<int> normalized(n);
        int maxValue = 0;
        for (int i = 0; i < n; i++) {
            normalized[i] = nums[i] / currentGcd;
            maxValue = max(maxValue, normalized[i]);
        }
        
        // 线性筛法预处理质数
        vector<bool> isPrime(maxValue + 1, true);
        isPrime[0] = isPrime[1] = false;
        
        for (int i = 2; i * i <= maxValue; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= maxValue; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        
        // 统计每个数出现的次数
        vector<int> count(maxValue + 1, 0);
        for (int num : normalized) {
            count[num]++;
        }
        
        // 枚举质数，统计是其倍数的数的个数
        int maxCount = 0;
        for (int i = 2; i <= maxValue; i++) {
            if (isPrime[i]) {
                int primeCount = 0;
                for (int j = i; j <= maxValue; j += i) {
                    primeCount += count[j];
                }
                maxCount = max(maxCount, primeCount);
            }
        }
        
        // 如果所有数都相同，则无法增大GCD
        if (maxCount == n) {
            return -1;
        }
        
        return n - maxCount;
    }
    
    /**
     * POJ 2429. GCD & LCM Inverse
     * 题目来源：http://poj.org/problem?id=2429
     * 问题描述：给定两个正整数a和b的最大公约数和最小公倍数，反过来求这两个数，要求这两个数的和最小。
     * 解题思路：设gcd为最大公约数，lcm为最小公倍数，则a*b = gcd*lcm。设a = gcd*x, b = gcd*y，
     *          则x*y = lcm/gcd，且gcd(x,y) = 1。问题转化为找到两个互质的数x和y，使得x*y = lcm/gcd，
     *          并且x+y最小。
     * 时间复杂度：O(√(lcm/gcd))
     * 空间复杂度：O(1)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static vector<long long> gcdLcmInverse(long long gcd, long long lcm) {
        // 计算lcm/gcd
        long long product = lcm / gcd;
        
        // 找到两个互质的数x和y，使得x*y = product，并且x+y最小
        long long x = 1;
        long long y = product;
        
        // 枚举所有可能的因子对
        for (long long i = 1; i * i <= product; i++) {
            if (product % i == 0) {
                long long factor1 = i;
                long long factor2 = product / i;
                
                // 检查这两个因子是否互质
                if (ExtendedGcdLcmProblems::gcd(factor1, factor2) == 1) {
                    // 如果当前因子对的和更小，则更新结果
                    if (factor1 + factor2 < x + y) {
                        x = factor1;
                        y = factor2;
                    }
                }
            }
        }
        
        // 返回结果，确保a <= b
        long long a = gcd * x;
        long long b = gcd * y;
        
        if (a > b) {
            long long temp = a;
            a = b;
            b = temp;
        }
        
        return {a, b};
    }
    
    /**
     * UVa 10892. LCM Cardinality
     * 题目来源：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1833
     * 问题描述：给定一个正整数n，找出有多少对不同的整数对(a,b)，使得lcm(a,b) = n。
     * 解题思路：枚举n的所有因子，对于每个因子d，如果gcd(d, n/d) = 1，则(d, n/d)是一对解。
     * 时间复杂度：O(√n)
     * 空间复杂度：O(1)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static int lcmCardinality(int n) {
        // 找到n的所有因子
        vector<int> divisors;
        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                divisors.push_back(i);
                if (i != n / i) {
                    divisors.push_back(n / i);
                }
            }
        }
        
        // 计算有多少对不同的整数对(a,b)使得lcm(a,b) = n
        int count = 0;
        for (int i = 0; i < divisors.size(); i++) {
            for (int j = i; j < divisors.size(); j++) {
                int a = divisors[i];
                int b = divisors[j];
                // 如果lcm(a,b) = n，则是一对解
                if (lcm(a, b) == n) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * SPOJ GCDEX. GCD Extreme
     * 题目来源：https://www.spoj.com/problems/GCDEX/
     * 问题描述：计算 G(n) = Σ(i=1 to n) Σ(j=i+1 to n) gcd(i, j)
     * 解题思路：使用欧拉函数优化计算
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static long long gcdExtreme(int n) {
        // 预处理欧拉函数
        vector<int> phi(n + 1);
        for (int i = 1; i <= n; i++) {
            phi[i] = i;
        }
        
        for (int i = 2; i <= n; i++) {
            if (phi[i] == i) { // i是质数
                for (int j = i; j <= n; j += i) {
                    phi[j] = phi[j] / i * (i - 1);
                }
            }
        }
        
        // 计算前缀和
        vector<long long> prefixSum(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + phi[i];
        }
        
        // 计算结果
        long long result = 0;
        for (int i = 1; i <= n; i++) {
            result += (long long)i * (prefixSum[n / i] - 1); // 减1是因为不包括phi[1]的情况
        }
        
        return result;
    }
    
    /**
     * LeetCode 2807. Insert Greatest Common Divisors in Linked List
     * 题目来源：https://leetcode.com/problems/insert-greatest-common-divisors-in-linked-list/
     * 问题描述：给定一个链表，在每对相邻节点之间插入一个值为它们最大公约数的新节点。
     * 解题思路：遍历链表，对每对相邻节点，计算它们的最大公约数并插入新节点。
     * 时间复杂度：O(n * log(min(a,b)))，其中n是链表长度
     * 空间复杂度：O(1)，只使用常数额外空间（不计算新插入的节点）
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static ListNode* insertGreatestCommonDivisors(ListNode* head) {
        // 如果链表为空或只有一个节点，直接返回
        if (head == nullptr || head->next == nullptr) {
            return head;
        }
        
        ListNode* current = head;
        
        // 遍历链表，直到倒数第二个节点
        while (current->next != nullptr) {
            // 计算当前节点和下一个节点值的最大公约数
            int gcdValue = gcd(current->val, current->next->val);
            
            // 创建新节点并插入
            ListNode* newNode = new ListNode(gcdValue);
            newNode->next = current->next;
            current->next = newNode;
            
            // 移动到下一个原始节点（跳过刚插入的节点）
            current = newNode->next;
        }
        
        return head;
    }
    
    /**
     * LeetCode 1979. Find Greatest Common Divisor of Array
     * 题目来源：https://leetcode.com/problems/find-greatest-common-divisor-of-array/
     * 问题描述：给定一个整数数组nums，返回数组中最小数和最大数的最大公约数。
     * 解题思路：首先找到数组中的最小值和最大值，然后计算它们的最大公约数。
     * 时间复杂度：O(n + log(min(min_val, max_val)))，其中n是数组长度
     * 空间复杂度：O(log(min(min_val, max_val)))，递归调用栈的深度
     * 是否最优解：是，这是解决该问题的最优方法。
     */
    static int findGCD(vector<int>& nums) {
        // 找到数组中的最小值和最大值
        int minVal = INT_MAX;
        int maxVal = INT_MIN;
        
        for (int num : nums) {
            minVal = min(minVal, num);
            maxVal = max(maxVal, num);
        }
        
        // 计算最小值和最大值的最大公约数
        return gcd(minVal, maxVal);
    }
    
    /**
     * LeetCode 878. 第N个神奇数字
     * 问题描述：一个正整数如果能被a或b整除，那么它是神奇的。给定n,a,b，返回第n个神奇数字。
     * 解题思路：使用二分查找 + 容斥原理
     * 时间复杂度：O(log(n * min(a,b)))
     * 空间复杂度：O(1)
     */
    static int nthMagicalNumber(int n, int a, int b) {
        long long lcm_val = lcm(a, b);
        long long left = 0;
        long long right = (long long)n * min(a, b);
        long long result = 0;
        
        // 二分查找第n个神奇数字
        while (left <= right) {
            long long mid = left + (right - left) / 2;
            // 在[1, mid]范围内神奇数字的个数
            long long count = mid / a + mid / b - mid / lcm_val;
            
            if (count >= n) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return (int)(result % 1000000007);
    }
    
    /**
     * LeetCode 1201. 丑数III
     * 问题描述：编写一个程序，找出第n个丑数，丑数是可以被a或b或c整除的正整数。
     * 解题思路：二分查找 + 容斥原理
     * 时间复杂度：O(log(n * min(a,b,c)))
     * 空间复杂度：O(1)
     */
    static int nthUglyNumber(int n, int a, int b, int c) {
        long long la = a, lb = b, lc = c;
        long long lab = lcm(la, lb);
        long long lac = lcm(la, lc);
        long long lbc = lcm(lb, lc);
        long long labc = lcm(lab, lc);
        
        long long left = 0;
        long long right = (long long)2e9; // 根据题目数据范围设定
        long long result = 0;
        
        // 二分查找第n个丑数
        while (left <= right) {
            long long mid = left + (right - left) / 2;
            // 在[1, mid]范围内丑数的个数（容斥原理）
            long long count = mid / la + mid / lb + mid / lc 
                            - mid / lab - mid / lac - mid / lbc 
                            + mid / labc;
            
            if (count >= n) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return (int)(result % 1000000007);
    }
    
    /**
     * LeetCode 1071. 字符串的最大公因子
     * 问题描述：对于字符串s和t，只有在s=t+t+t+...+t时，才认为t能除尽s。
     *           给定两个字符串str1和str2，返回最长字符串x，使得x能除尽str1和str2。
     * 解题思路：利用GCD的性质，如果存在这样的字符串，其长度必然是两个字符串长度的GCD
     * 时间复杂度：O(m+n)
     * 空间复杂度：O(1)
     */
    static string gcdOfStrings(string str1, string str2) {
        // 如果存在公因子字符串，则str1+str2应该等于str2+str1
        if (str1 + str2 != str2 + str1) {
            return "";
        }
        
        // 最大公因子字符串的长度就是两个字符串长度的GCD
        int gcdLength = gcd((int)str1.length(), (int)str2.length());
        return str1.substr(0, gcdLength);
    }
    
    /**
     * LeetCode 2447. 最大公因数等于K的子数组数目
     * 问题描述：给定一个数组和一个正整数k，返回最大公因数等于k的子数组数目。
     * 解题思路：遍历所有子数组，计算每个子数组的GCD，统计等于k的数量
     * 时间复杂度：O(n^2 * log(max(nums)))
     * 空间复杂度：O(1)
     */
    static int subarrayGCD(vector<int>& nums, int k) {
        int count = 0;
        int n = nums.size();
        
        // 遍历所有可能的子数组
        for (int i = 0; i < n; i++) {
            int currentGcd = nums[i];
            // 优化：如果当前元素不能被k整除，跳过
            if (currentGcd % k != 0) continue;
            
            for (int j = i; j < n; j++) {
                // 优化：如果当前元素不能被k整除，跳出内层循环
                if (nums[j] % k != 0) break;
                
                currentGcd = gcd(currentGcd, nums[j]);
                
                // 如果GCD小于k，不可能再变大，跳出内层循环
                if (currentGcd < k) break;
                
                // 如果GCD等于k，计数加1
                if (currentGcd == k) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * LeetCode 2470. 最小公倍数为K的子数组数目
     * 问题描述：给定一个数组和一个正整数k，返回最小公倍数等于k的子数组数目。
     * 解题思路：遍历所有子数组，计算每个子数组的LCM，统计等于k的数量
     * 时间复杂度：O(n^2 * log(max(nums)))
     * 空间复杂度：O(1)
     */
    static int subarrayLCM(vector<int>& nums, int k) {
        int count = 0;
        int n = nums.size();
        
        // 遍历所有可能的子数组
        for (int i = 0; i < n; i++) {
            long long currentLcm = nums[i];
            
            // 如果当前元素不能整除k，跳过
            if (k % nums[i] != 0) continue;
            
            for (int j = i; j < n; j++) {
                // 如果当前元素不能整除k，跳出内层循环
                if (k % nums[j] != 0) break;
                
                currentLcm = lcm(currentLcm, nums[j]);
                
                // 如果LCM大于k，不可能再变小，跳出内层循环
                if (currentLcm > k) break;
                
                // 如果LCM等于k，计数加1
                if (currentLcm == k) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 计算最大公约数（欧几里得算法）- 整型版本
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))（递归）
     */
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 计算最大公约数（欧几里得算法）- 长整型版本
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))（递归）
     */
    static long long gcd(long long a, long long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 计算最小公倍数
     * 利用公式：lcm(a,b) = |a*b| / gcd(a,b)
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))
     */
    static long long lcm(long long a, long long b) {
        return a / gcd(a, b) * b;
    }
    
    /**
     * 扩展欧几里得算法
     * 求解 ax + by = gcd(a,b) 的一组整数解
     * 同时返回gcd(a,b)的值
     * 时间复杂度：O(log(min(a,b)))
     * 空间复杂度：O(log(min(a,b)))
     */
    static vector<long long> extendedGcd(long long a, long long b) {
        if (b == 0) {
            return {a, 1, 0}; // gcd, x, y
        }
        
        vector<long long> result = extendedGcd(b, a % b);
        long long gcd_val = result[0];
        long long x1 = result[1];
        long long y1 = result[2];
        
        long long x = y1;
        long long y = x1 - (a / b) * y1;
        
        return {gcd_val, x, y};
    }
    
    /**
     * 计算数组中所有元素的最大公约数
     * 时间复杂度：O(n * log(min(elements)))
     * 空间复杂度：O(log(min(elements)))
     */
    static int gcdOfArray(vector<int>& nums) {
        int result = nums[0];
        for (int i = 1; i < nums.size(); i++) {
            result = gcd(result, nums[i]);
            // 优化：如果GCD已经为1，可以提前结束
            if (result == 1) break;
        }
        return result;
    }
    
    /**
     * 计算数组中所有元素的最小公倍数
     * 时间复杂度：O(n * log(min(elements)))
     * 空间复杂度：O(log(min(elements)))
     */
    static long long lcmOfArray(vector<int>& nums) {
        long long result = nums[0];
        for (int i = 1; i < nums.size(); i++) {
            result = lcm(result, nums[i]);
        }
        return result;
    }
};

// 辅助方法：打印链表
void printList(ListNode* head) {
    ListNode* current = head;
    while (current != nullptr) {
        cout << current->val;
        if (current->next != nullptr) {
            cout << " -> ";
        }
        current = current->next;
    }
    cout << endl;
}

// 测试方法
int main() {
    cout << "=== GCD和LCM扩展问题测试 ===" << endl;
    
    // 测试gcdLcmInverse
    vector<long long> result = ExtendedGcdLcmProblems::gcdLcmInverse(3, 60);
    cout << "GCD & LCM Inverse (gcd=3, lcm=60): a=" << result[0] << ", b=" << result[1] << endl;
    
    result = ExtendedGcdLcmProblems::gcdLcmInverse(2, 20);
    cout << "GCD & LCM Inverse (gcd=2, lcm=20): a=" << result[0] << ", b=" << result[1] << endl;
    
    // 测试lcmCardinality
    cout << "LCM Cardinality (n=2): " << ExtendedGcdLcmProblems::lcmCardinality(2) << endl;
    cout << "LCM Cardinality (n=12): " << ExtendedGcdLcmProblems::lcmCardinality(12) << endl;
    cout << "LCM Cardinality (n=100): " << ExtendedGcdLcmProblems::lcmCardinality(100) << endl;
    
    // 测试gcdExtreme
    cout << "GCD Extreme (n=3): " << ExtendedGcdLcmProblems::gcdExtreme(3) << endl;
    cout << "GCD Extreme (n=4): " << ExtendedGcdLcmProblems::gcdExtreme(4) << endl;
    cout << "GCD Extreme (n=6): " << ExtendedGcdLcmProblems::gcdExtreme(6) << endl;
    
    // 测试insertGreatestCommonDivisors
    ListNode* head1 = new ListNode(18);
    head1->next = new ListNode(6);
    head1->next->next = new ListNode(10);
    head1->next->next->next = new ListNode(3);
    
    cout << "原链表: ";
    printList(head1);
    
    ListNode* result1 = ExtendedGcdLcmProblems::insertGreatestCommonDivisors(head1);
    cout << "插入GCD后: ";
    printList(result1);
    
    // 测试findGCD
    vector<int> nums1 = {2, 5, 6, 9, 10};
    cout << "数组[2,5,6,9,10]的GCD: " << ExtendedGcdLcmProblems::findGCD(nums1) << endl;
    
    vector<int> nums2 = {7, 5, 6, 8, 3};
    cout << "数组[7,5,6,8,3]的GCD: " << ExtendedGcdLcmProblems::findGCD(nums2) << endl;
    
    vector<int> nums3 = {3, 3};
    cout << "数组[3,3]的GCD: " << ExtendedGcdLcmProblems::findGCD(nums3) << endl;
    
    // 测试nthMagicalNumber
    cout << "第1个神奇数字(n=1, a=2, b=3): " << ExtendedGcdLcmProblems::nthMagicalNumber(1, 2, 3) << endl;
    cout << "第4个神奇数字(n=4, a=2, b=3): " << ExtendedGcdLcmProblems::nthMagicalNumber(4, 2, 3) << endl;
    
    // 测试nthUglyNumber
    cout << "第3个丑数(n=3, a=2, b=3, c=5): " << ExtendedGcdLcmProblems::nthUglyNumber(3, 2, 3, 5) << endl;
    cout << "第4个丑数(n=4, a=2, b=3, c=4): " << ExtendedGcdLcmProblems::nthUglyNumber(4, 2, 3, 4) << endl;
    
    // 测试gcdOfStrings
    cout << "字符串最大公因子(\"ABCABC\", \"ABC\"): " << ExtendedGcdLcmProblems::gcdOfStrings("ABCABC", "ABC") << endl;
    cout << "字符串最大公因子(\"ABABAB\", \"ABAB\"): " << ExtendedGcdLcmProblems::gcdOfStrings("ABABAB", "ABAB") << endl;
    cout << "字符串最大公因子(\"LEET\", \"CODE\"): " << ExtendedGcdLcmProblems::gcdOfStrings("LEET", "CODE") << endl;
    
    // 测试subarrayGCD
    vector<int> nums4 = {9, 3, 1, 2, 6, 3};
    cout << "GCD等于3的子数组数目: " << ExtendedGcdLcmProblems::subarrayGCD(nums4, 3) << endl;
    
    vector<int> nums5 = {3, 1, 2, 4, 6};
    cout << "GCD等于1的子数组数目: " << ExtendedGcdLcmProblems::subarrayGCD(nums5, 1) << endl;
    
    // 测试subarrayLCM
    vector<int> nums6 = {3, 6, 2, 1, 2};
    cout << "LCM等于6的子数组数目: " << ExtendedGcdLcmProblems::subarrayLCM(nums6, 6) << endl;
    
    // 测试extendedGcd
    vector<long long> extResult = ExtendedGcdLcmProblems::extendedGcd(30, 18);
    cout << "扩展欧几里得算法(30, 18): gcd=" << extResult[0] << 
            ", x=" << extResult[1] << ", y=" << extResult[2] << endl;
    cout << "验证: 30*" << extResult[1] << " + 18*" << extResult[2] << 
            " = " << (30*extResult[1] + 18*extResult[2]) << endl;
    
    // 测试数组GCD和LCM
    vector<int> nums7 = {12, 18, 24};
    cout << "数组[12,18,24]的GCD: " << ExtendedGcdLcmProblems::gcdOfArray(nums7) << endl;
    cout << "数组[12,18,24]的LCM: " << ExtendedGcdLcmProblems::lcmOfArray(nums7) << endl;
    
    // 测试数组GCD和LCM
    vector<int> nums8 = {6, 12, 18};
    cout << "Enlarge GCD (nums=[6,12,18]): " << ExtendedGcdLcmProblems::enlargeGCD(nums8) << endl;
    
    return 0;
}

