package recursion.LeetCode;

/**
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级台阶。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
 * <p>
 * 答案需要取模 1e9+7（1000000007），如计算初始结果为：1000000008，请返回 1。
 * <p>
 * 示例 1：
 * 输入：n = 2
 * 输出：2
 * <p>
 * 示例 2：
 * 输入：n = 7
 * 输出：21
 * <p>
 * 示例 3：
 * 输入：n = 0
 * 输出：1
 * <p>
 * 提示：
 * 0 <= n <= 100
 * <p>
 * 题目链接：https://leetcode-cn.com/problems/qing-wa-tiao-tai-jie-wen-ti-lcof
 */
public class Offer_10_II {

    /**
     * 用递归的方法，n级台阶的所有走法，有两种情况，走了1步，则剩下n-1级台阶的走法；走了2步，则剩下n-2级台阶的走法
     * 那n级台阶的所有走法则是走了1步之后的走法加上走了2步之后的走法
     * 即 f(n)=f(n-1)+f(n-2)  ======> 递归公式
     * 终止条件则为 f(1)=1,f(2)=2，只剩下一级台阶时只有一种走法，剩下两级台阶时，可选择走一步，或者走两步，有两种走法
     * 直接递归会造成超时，要用记忆化递归
     * https://leetcode-cn.com/problems/qing-wa-tiao-tai-jie-wen-ti-lcof/solution/mian-shi-ti-10-ii-qing-wa-tiao-tai-jie-wen-ti-dong/
     * @param n
     * @return
     */
    public int numWays(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        return (int) ((numWays(n - 1)%(1e9+7)) + (numWays(n - 2)%(1e9+7)));
    }

}
