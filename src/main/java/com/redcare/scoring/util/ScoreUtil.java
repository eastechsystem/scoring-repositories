package com.redcare.scoring.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

import org.springframework.util.CollectionUtils;

import com.redcare.scoring.dto.RepositoryDto;
import com.redcare.scoring.model.Repository;

/**
 * This utility class is used to compute the popularity score and assign it to
 * each repository.
 *
 * @author Jahanzaib Ali
 * @since 05.01.2025
 */
public class ScoreUtil {

    /**
     * Are weights for stars, forks, and recency, respectively, and their sum = 1
     */
    private static final double[] ADJUSTABLE_WEIGHTS = {0.4, 0.3, 0.3};

    private ScoreUtil() {
    }

    /**
     * <b> calculateScores</b> method is use to calculate the popularity score on
     * the basis of normalized stars, forks and Recency of update.</br>
     * <p>
     * And it use to assign the computed score to each repository. It use some
     * internal calculation formulas for computing the score. i.e. </br></br>
     * <p>
     * 1. <b>StarsScore = Stars / MaxStars ​</b></br>
     * 2. <b>ForksScore = Forks / MaxForks ​</b></br>
     * 3. <b>RecencyScore = log (1 + x​) ​</b> where x = (maxDays - daysSinceUpdate) / maxDays </br>
     * 4. <b>PopularityScore = (W1 * StarsScore) + (W2 * ForksScore) + (W3 * RecencyScore)​ ​</b></br>
     *
     * @param repositories type of {@link List}
     * @return list type of {@link RepositoryDto}
     */
    public static List<RepositoryDto> calculatePopularityScore(final List<Repository> repositories) {
        if (CollectionUtils.isEmpty(repositories)) {
            return Collections.emptyList();
        }

        int maxStars = getMaxValue(repositories, Repository::getStargazersCount);
        int maxForks = getMaxValue(repositories, Repository::getForksCount);
        long maxDaysSinceUpdate = getMaxDaysSinceUpdate(repositories);


        return repositories.stream()
                .map(repo -> calculateRepositoryDto(repo, maxStars, maxForks, maxDaysSinceUpdate))
                .toList();
    }

    /**
     * <b> getMaxValue()</b> method is use to compute maximum value from
     * repositories.
     *
     * @param repositories type of {@link List}
     * @param mapper       type of {@link ToIntFunction}
     * @return value type of int
     */
    private static int getMaxValue(final List<Repository> repositories,
                                   final ToIntFunction<Repository> mapper) {
        return repositories.stream()
                .mapToInt(mapper)
                .max()
                .orElse(0);
    }

    /**
     * <b> getMaxDaysSinceUpdate()</b> method is use to compute maximum days till
     * the current date from repositories.
     *
     * @param repositories type of {@link List}
     * @return value type of long
     */
    private static long getMaxDaysSinceUpdate(final List<Repository> repositories) {
        return repositories.stream()
                .mapToLong(repo -> calculateDaysSinceUpdate(repo.getUpdatedAt()))
                .max()
                .orElse(0);
    }

    /**
     * <b> calculateRepositoryDto()</b> method is use to create RepositoryDto object
     * for popularityScore from repositories, maxStars, maxForks and
     * maxDaysSinceUpdate.
     *
     * @param repository         type of {@link Repository}
     * @param maxStars           type of int
     * @param maxForks           type of int
     * @param maxDaysSinceUpdate type of long
     * @return object type of long {@link RepositoryDto}
     */
    private static RepositoryDto calculateRepositoryDto(final Repository repository,
                                                        int maxStars,
                                                        int maxForks,
                                                        long maxDaysSinceUpdate) {

        int stars = repository.getStargazersCount();
        int forks = repository.getForksCount();
        final String updatedAt = repository.getUpdatedAt();

        // Normalized values
        double normalizedStars = normalize(stars, maxStars);
        double normalizedForks = normalize(forks, maxForks);

        // Recency score
        long daysSinceUpdate = calculateDaysSinceUpdate(updatedAt);
        double recencyScore = calculateRecencyScoreByLog(daysSinceUpdate, maxDaysSinceUpdate);

        // Popularity score
        double popularityScore = calculatePopularityScore(normalizedStars, normalizedForks, recencyScore);

        return new RepositoryDto(repository.getName(), repository.getOwner().getLogin(), stars, forks, updatedAt, popularityScore);
    }

    /**
     * The <b>normalized</b> score for stars and forks is calculated with formula :</br>
     * 1. <b>StarsScore = Stars / MaxStars ​</b></br>
     * 2. <b>ForksScore = Forks / MaxForks ​</b></br></br>
     *
     * <b> Reference for stated formula as a source: </b> <a href=
     * "https://american-cse.org/sites/csci2020proc/pdfs/CSCI2020-6SccvdzjqC7bKupZxFmCoA/762400a217/762400a217.pdf">Scoring
     * Popularity in GitHub</a>
     *
     * @param value    type of int
     * @param maxValue type of long
     * @return value type of double
     */
    private static double normalize(int value,
                                    int maxValue) {
        return maxValue == 0 ? 0 : (double) value / maxValue;
    }


    /**
     * <b> calculateRecencyScoreByLog()</b> method is use to calculate receny score
     * by using logarithmic formula :</br>
     * <b>RecencyScore = log (1 + x​) ​</b> where x = (maxDays - daysSinceUpdate) / maxDays </br></br>
     *
     * <b> Reference for stated formula as a source: </b></br>
     * 1. <a href= "https://pdfs.semanticscholar.org/ad57/ee8f50b27944c2b2b370a43cb27fb712c1aa.pdf">decay-based ranking</a></br>
     * 2. <a href= "https://julesjacobs.com/2015/05/06/exponentially-decaying-likes.html?utm_source=chatgpt.com">Exponentially decaying -> Math to the rescue</a></br>
     * 3. <a href= "https://stackoverflow.com/questions/11653545/hot-content-algorithm-score-with-time-decay?utm_source=chatgpt.com">Discussion about time decay for ranking content</a>
     *
     * @param daysSinceUpdate type of long
     * @param maxDays         type of long
     * @return value type of double
     */
    private static double calculateRecencyScoreByLog(long daysSinceUpdate,
                                                     long maxDays) {
        if (maxDays == 0) {
            return 0;
        }

        double x = (maxDays - daysSinceUpdate) / maxDays;
        return maxDays == 0 ? 0 : Math.log(1 + x);
    }

    /**
     * <b> calculatePopularityScore()</b> method is use to calculate overall
     * popularity score by using formula :</br>
     * <b>PopularityScore = (W1 * StarsScore) + (W2 * ForksScore) + (W3 * RecencyScore)​ ​</b></br></br>
     *
     * <b> Reference for stated formula as a source: </b> <a href=
     * "https://american-cse.org/sites/csci2020proc/pdfs/CSCI2020-6SccvdzjqC7bKupZxFmCoA/762400a217/762400a217.pdf">Scoring
     * Popularity in GitHub</a>
     *
     * @param normalizedStars type of double
     * @param normalizedForks type of double
     * @param recencyScore    type of double
     * @return value type of double
     */
    private static double calculatePopularityScore(double normalizedStars,
                                                   double normalizedForks,
                                                   double recencyScore) {
        return (ADJUSTABLE_WEIGHTS[0] * normalizedStars)
                + (ADJUSTABLE_WEIGHTS[1] * normalizedForks)
                + (ADJUSTABLE_WEIGHTS[2] * recencyScore);
    }

    /**
     * <b>calculateDaysSinceUpdate()</b> method is use to calculate number of days
     * between updated date of repository and till the current date.
     *
     * @param updatedAt type of {@link String}
     * @return value type of long
     */
    private static long calculateDaysSinceUpdate(String updatedAt) {
        LocalDateTime updateTime = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_DATE_TIME);
        return ChronoUnit.DAYS.between(updateTime, LocalDateTime.now());
    }
}
