    @staticmethod
    def find_restaurant(list1: List[str], list2: List[str]) -> List[str]:
        """
        LeetCode 599. Minimum Index Sum of Two Lists (两个列表的最小索引总和)
        题目来源: https://leetcode.com/problems/minimum-index-sum-of-two-lists/
        
        题目描述:
        假设Andy和Doris想在晚餐时选择一家餐厅，并且他们都有一个表示最喜爱餐厅的列表，每个餐厅的名字用字符串表示。
        你需要帮助他们用最少的索引和找出他们共同喜爱的餐厅。 如果答案不止一个，则输出所有答案并且不考虑顺序。
        
        示例:
        输入: list1 = ["Shogun", "Tapioca Express", "Burger King", "KFC"]
                list2 = ["Piatti", "The Grill at Torrey Pines", "Hungry Hunter Steakhouse", "Shogun"]
        输出: ["Shogun"]
        解释: 他们唯一共同喜爱的餐厅是"Shogun"。
        
        算法思路:
        1. 使用哈希表存储list1中每个餐厅的名称和索引
        2. 遍历list2，查找共同喜爱的餐厅，并计算索引和
        3. 返回索引和最小的餐厅
        
        时间复杂度: O(n+m)，其中n和m分别是两个列表的长度
        空间复杂度: O(n)
        """
        # 存储list1中餐厅名称和索引
        restaurant_index = {}
        for i, restaurant in enumerate(list1):
            restaurant_index[restaurant] = i
        
        min_index_sum = float('inf')
        result = []
        
        # 遍历list2，查找共同喜爱的餐厅
        for j, restaurant in enumerate(list2):
            if restaurant in restaurant_index:
                index_sum = restaurant_index[restaurant] + j
                
                # 如果找到更小的索引和，更新结果
                if index_sum < min_index_sum:
                    min_index_sum = index_sum
                    result = [restaurant]
                # 如果索引和相等，添加到结果中
                elif index_sum == min_index_sum:
                    result.append(restaurant)
        
        return result

    @staticmethod
    def find_common_characters(words: List[str]) -> List[str]:
        """
        LeetCode 1002. Find Common Characters (查找共用字符)
        题目来源: https://leetcode.com/problems/find-common-characters/
        
        题目描述:
        给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。
        例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
        你可以按任意顺序返回答案。
        
        示例:
        输入：["bella","label","roller"]
        输出：["e","l","l"]
        
        输入：["cool","lock","cook"]
        输出：["c","o"]
        
        算法思路:
        1. 使用计数器统计每个单词中字符出现的次数
        2. 对于每个字符，取所有单词中出现次数的最小值
        3. 根据最小出现次数构建结果列表
        
        时间复杂度: O(n*k)，其中n是单词数量，k是单词的平均长度
        空间复杂度: O(1)，因为字符集固定为26个小写字母
        """
        if not words:
            return []
        
        # 统计第一个单词中每个字符出现的次数
        char_count = {}
        for char in words[0]:
            char_count[char] = char_count.get(char, 0) + 1
        
        # 对于其他单词，更新每个字符出现的最小次数
        for i in range(1, len(words)):
            word_count = {}
            for char in words[i]:
                word_count[char] = word_count.get(char, 0) + 1
            
            # 更新最小出现次数
            for char in list(char_count.keys()):
                if char in word_count:
                    char_count[char] = min(char_count[char], word_count[char])
                else:
                    del char_count[char]
        
        # 构建结果列表
        result = []
        for char, count in char_count.items():
            result.extend([char] * count)
        
        return result

    @staticmethod
    def most_common_word(paragraph: str, banned: List[str]) -> str:
        """
        LeetCode 819. Most Common Word (最常见的单词)
        题目来源: https://leetcode.com/problems/most-common-word/
        
        题目描述:
        给定一个段落 (paragraph) 和一个禁用单词列表 (banned)。返回出现次数最多，同时不在禁用列表中的单词。
        题目保证至少有一个词不在禁用列表中，而且答案唯一。
        禁用列表中的单词用小写字母表示，不含标点符号。段落中的单词不区分大小写。答案都是小写字母。
        
        示例:
        输入: 
        paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
        banned = ["hit"]
        输出: "ball"
        解释: 
        "hit" 出现了3次，但它是一个禁用的单词。
        "ball" 出现了2次 (同时没有其他单词出现2次)，所以它是段落里出现次数最多的，且不在禁用列表中的单词。 
        
        算法思路:
        1. 将段落转换为小写，并替换所有标点符号为空格
        2. 分割段落为单词，并统计每个单词出现的次数
        3. 排除禁用单词，返回出现次数最多的单词
        
        时间复杂度: O(n)，其中n是段落的长度
        空间复杂度: O(n)
        """
        # 将段落转换为小写，并替换所有标点符号为空格
        for c in "!?',;.":
            paragraph = paragraph.replace(c, " ")
        
        # 分割段落为单词，并转换为小写
        words = paragraph.lower().split()
        
        # 创建禁用单词集合，便于快速查找
        banned_set = set(banned)
        
        # 统计单词出现次数
        word_count = {}
        for word in words:
            if word not in banned_set:
                word_count[word] = word_count.get(word, 0) + 1
        
        # 返回出现次数最多的单词
        return max(word_count.items(), key=lambda x: x[1])[0]

    @staticmethod
    def subdomain_visit_count(cpdomains: List[str]) -> List[str]:
        """
        LeetCode 811. Subdomain Visit Count (子域名访问计数)
        题目来源: https://leetcode.com/problems/subdomain-visit-count/
        
        题目描述:
        一个网站域名，如"discuss.leetcode.com"，包含了多个子域名。作为顶级域名，常用的有"com"，下一级则有"leetcode.com"，
        最低的一级为"discuss.leetcode.com"。当我们访问域名"discuss.leetcode.com"时，也同时访问了其父域名"leetcode.com"以及顶级域名 "com"。
        
        给定一个带访问次数和域名的组合，要求分别计算每个域名被访问的次数。其格式为访问次数+空格+地址，例如："9001 discuss.leetcode.com"。
        
        接下来会给出一组访问次数和域名组合的列表cpdomains。要求解析出所有域名的访问次数，输出格式和输入格式相同，不限定先后顺序。
        
        示例:
        输入: ["9001 discuss.leetcode.com"]
        输出: ["9001 discuss.leetcode.com", "9001 leetcode.com", "9001 com"]
        解释: 
        例子中仅包含一个网站域名："discuss.leetcode.com"。
        按照前文假设，子域名"leetcode.com"和"com"都会被访问，所以它们都被访问了9001次。
        
        算法思路:
        1. 解析每个域名组合，提取访问次数和完整域名
        2. 分割域名，统计每个子域名的访问次数
        3. 格式化输出结果
        
        时间复杂度: O(n)，其中n是域名组合的总长度
        空间复杂度: O(m)，其中m是不同子域名的数量
        """
        # 统计每个子域名的访问次数
        domain_count = {}
        
        for cpdomain in cpdomains:
            # 解析访问次数和完整域名
            count, domain = cpdomain.split()
            count = int(count)
            
            # 分割域名，统计每个子域名的访问次数
            parts = domain.split('.')
            for i in range(len(parts)):
                subdomain = '.'.join(parts[i:])
                domain_count[subdomain] = domain_count.get(subdomain, 0) + count
        
        # 格式化输出结果
        result = []
        for domain, count in domain_count.items():
            result.append(f"{count} {domain}")
        
        return result

    @staticmethod
    def uncommon_words_from_two_sentences(s1: str, s2: str) -> List[str]:
        """
        LeetCode 884. Uncommon Words from Two Sentences (两句话中的不常见单词)
        题目来源: https://leetcode.com/problems/uncommon-words-from-two-sentences/
        
        题目描述:
        给定两个句子 A 和 B，返回所有在句子 A 和 B 中都只出现一次的单词。
        你可以按任意顺序返回答案。
        
        示例:
        输入：A = "this apple is sweet", B = "this apple is sour"
        输出：["sweet","sour"]
        
        输入：A = "apple apple", B = "banana"
        输出：["banana"]
        
        算法思路:
        1. 将两个句子合并，分割为单词
        2. 统计每个单词出现的次数
        3. 返回只出现一次的单词
        
        时间复杂度: O(n+m)，其中n和m分别是两个句子的长度
        空间复杂度: O(n+m)
        """
        # 将两个句子合并，分割为单词
        words = s1.split() + s2.split()
        
        # 统计每个单词出现的次数
        word_count = {}
        for word in words:
            word_count[word] = word_count.get(word, 0) + 1
        
        # 返回只出现一次的单词
        return [word for word, count in word_count.items() if count == 1]

    @staticmethod
    def intersection_of_two_arrays(nums1: List[int], nums2: List[int]) -> List[int]:
        """
        LeetCode 349. Intersection of Two Arrays (两个数组的交集)
        题目来源: https://leetcode.com/problems/intersection-of-two-arrays/
        
        题目描述:
        给定两个数组，编写一个函数来计算它们的交集。
        
        示例:
        输入：nums1 = [1,2,2,1], nums2 = [2,2]
        输出：[2]
        
        输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
        输出：[9,4]
        
        说明：
        输出结果中的每个元素一定是唯一的。
        我们可以不考虑输出结果的顺序。
        
        算法思路:
        1. 使用集合存储nums1中的元素
        2. 遍历nums2，找出同时存在于nums1中的元素
        3. 使用集合去重
        
        时间复杂度: O(n+m)，其中n和m分别是两个数组的长度
        空间复杂度: O(n+m)
        """
        # 使用集合存储nums1中的元素
        set1 = set(nums1)
        
        # 找出同时存在于nums1和nums2中的元素
        result = set()
        for num in nums2:
            if num in set1:
                result.add(num)
        
        return list(result)

    @staticmethod
    def intersection_of_two_arrays_ii(nums1: List[int], nums2: List[int]) -> List[int]:
        """
        LeetCode 350. Intersection of Two Arrays II (两个数组的交集 II)
        题目来源: https://leetcode.com/problems/intersection-of-two-arrays-ii/
        
        题目描述:
        给定两个数组，编写一个函数来计算它们的交集。
        
        示例:
        输入：nums1 = [1,2,2,1], nums2 = [2,2]
        输出：[2,2]
        
        输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
        输出：[4,9]
        
        说明：
        输出结果中每个元素出现的次数，应与元素在两个数组中出现次数的最小值一致。
        我们可以不考虑输出结果的顺序。
        
        算法思路:
        1. 使用哈希表统计nums1中每个元素出现的次数
        2. 遍历nums2，如果元素在哈希表中且计数大于0，则添加到结果中，并减少计数
        
        时间复杂度: O(n+m)，其中n和m分别是两个数组的长度
        空间复杂度: O(min(n,m))
        """
        # 使用哈希表统计nums1中每个元素出现的次数
        counter = {}
        for num in nums1:
            counter[num] = counter.get(num, 0) + 1
        
        # 遍历nums2，找出交集元素
        result = []
        for num in nums2:
            if num in counter and counter[num] > 0:
                result.append(num)
                counter[num] -= 1
        
        return result

    @staticmethod
    def keyboard_row(words: List[str]) -> List[str]:
        """
        LeetCode 500. Keyboard Row (键盘行)
        题目来源: https://leetcode.com/problems/keyboard-row/
        
        题目描述:
        给你一个字符串数组 words，只返回可以使用在美式键盘同一行的字母打印出来的单词。
        
        美式键盘的三行分别是：
        第一行：'qwertyuiop'
        第二行：'asdfghjkl'
        第三行：'zxcvbnm'
        
        示例:
        输入：words = ["Hello","Alaska","Dad","Peace"]
        输出：["Alaska","Dad"]
        
        算法思路:
        1. 创建三个集合，分别存储键盘的三行字母
        2. 对于每个单词，检查其所有字母是否都在同一行
        
        时间复杂度: O(n)，其中n是所有单词的总长度
        空间复杂度: O(1)，因为键盘行是固定的
        """
        # 创建三个集合，存储键盘的三行字母
        row1 = set("qwertyuiop")
        row2 = set("asdfghjkl")
        row3 = set("zxcvbnm")
        
        result = []
        
        for word in words:
            # 转换为小写，便于比较
            w = word.lower()
            
            # 检查单词的所有字母是否都在同一行
            if all(c in row1 for c in w) or all(c in row2 for c in w) or all(c in row3 for c in w):
                result.append(word)
        
        return result

    @staticmethod
    def jewels_and_stones(jewels: str, stones: str) -> int:
        """
        LeetCode 771. Jewels and Stones (宝石与石头)
        题目来源: https://leetcode.com/problems/jewels-and-stones/
        
        题目描述:
        给定字符串J 代表石头中宝石的类型，和字符串 S代表你拥有的石头。
        S 中每个字符代表了一种你拥有的石头的类型，你想知道你拥有的石头中有多少是宝石。
        J 中的字母不重复，J 和 S中的所有字符都是字母。字母区分大小写，因此"a"和"A"是不同类型的石头。
        
        示例:
        输入: J = "aA", S = "aAAbbbb"
        输出: 3
        
        算法思路:
        1. 使用集合存储宝石类型，便于快速查找
        2. 遍历石头，统计宝石数量
        
        时间复杂度: O(J+S)，其中J和S分别是两个字符串的长度
        空间复杂度: O(J)
        """
        # 使用集合存储宝石类型
        jewel_set = set(jewels)
        
        # 统计宝石数量
        count = 0
        for stone in stones:
            if stone in jewel_set:
                count += 1
        
        return count

    @staticmethod
    def longest_consecutive_sequence(nums: List[int]) -> int:
        """
        LeetCode 128. Longest Consecutive Sequence (最长连续序列)
        题目来源: https://leetcode.com/problems/longest-consecutive-sequence/
        
        题目描述:
        给定一个未排序的整数数组 nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
        请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
        
        示例:
        输入：nums = [100,4,200,1,3,2]
        输出：4
        解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
        
        算法思路:
        1. 使用哈希集合存储所有数字，便于O(1)时间查找
        2. 对于每个数字x，如果x-1不在集合中（说明x是序列的起点），则尝试找出以x开始的最长序列
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(n)
        """
        if not nums:
            return 0
        
        # 使用哈希集合存储所有数字
        num_set = set(nums)
        
        max_length = 0
        
        # 对于每个数字，检查它是否是序列的起点
        for num in num_set:
            # 如果num-1不在集合中，说明num是序列的起点
            if num - 1 not in num_set:
                current_num = num
                current_length = 1
                
                # 尝试找出以num开始的最长序列
                while current_num + 1 in num_set:
                    current_num += 1
                    current_length += 1
                
                # 更新最长序列长度
                max_length = max(max_length, current_length)
        
        return max_length