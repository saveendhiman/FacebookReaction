package com.facebookreactions.reaction;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.facebookreactions.R;
import com.facebookreactions.util.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by saveen_dhiman on 15-August-17.
 */

public class ReactionView extends View {

  private static final int SCALE_DURATION = 200;
  private static final int TRANSLATION_DURATION = 800;
  private static final int CHILD_TRANSLATION_DURATION = 300;
  private static final int CHILD_DELAY = 100;
  private static final int DRAW_DELAY = 50;
  private RoundedBoard board;
  private List<Emotion> emotions;
  private int selectedIndex = -1;
  private int selectedReactionIndex = -1;

  private SelectingAnimation selectingAnimation;
  private DeselectAnimation deselectAnimation;
  private SelectedReaction selectedReaction;

  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      invalidate();
    }
  };

  public ReactionView(Context context, SelectedReaction selectedReaction) {
    super(context);
    this.selectedReaction = selectedReaction;
    init();
  }

  public ReactionView(Context context, AttributeSet attrs, SelectedReaction selectedReaction) {
    super(context, attrs);
    this.selectedReaction = selectedReaction;
    init();
  }

  public ReactionView(Context context, AttributeSet attrs, int defStyleAttr, SelectedReaction selectedReaction) {
    super(context, attrs, defStyleAttr);
    this.selectedReaction = selectedReaction;
    init();
  }

  private void init() {
    board = new RoundedBoard();
    setLayerType(LAYER_TYPE_SOFTWARE, null);

    emotions = Arrays.asList(

            new Emotion(getContext(), "Like", ContextCompat.getDrawable(getContext(), R.drawable.like)),
            new Emotion(getContext(), "Love", ContextCompat.getDrawable(getContext(), R.drawable.love)),
            new Emotion(getContext(), "Haha", ContextCompat.getDrawable(getContext(), R.drawable.haha)),
            new Emotion(getContext(), "Wow",ContextCompat.getDrawable(getContext(), R.drawable.wow)),
            new Emotion(getContext(), "Sorry",ContextCompat.getDrawable(getContext(), R.drawable.sad)),
            new Emotion(getContext(), "Angry",ContextCompat.getDrawable(getContext(), R.drawable.angry))
       );

    selectingAnimation = new SelectingAnimation();
    deselectAnimation = new DeselectAnimation();
    deselectAnimation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {}

      @Override
      public void onAnimationEnd(Animation animation) {
          selectedIndex = -1;
      }
      @Override
      public void onAnimationRepeat(Animation animation) {}
    });

    startAnimation(new TranslationAnimation());
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        return true;
      case MotionEvent.ACTION_MOVE:
        for (int i = 0; i < emotions.size(); i++) {
          if (event.getX() > emotions.get(i).x &&
              event.getX() < emotions.get(i).x + emotions.get(i).size) {
            onSelect(i);
            selectedReactionIndex = i;
            break;
          }
        }
        return true;
      case MotionEvent.ACTION_UP:
       String reaction = "";
        if (selectedReactionIndex == 0){
          reaction = "like";
        }else if (selectedReactionIndex == 1){
        reaction = "love";
        }else if (selectedReactionIndex == 2){
          reaction = "haha";
        }else if (selectedReactionIndex == 3){
          reaction = "wow";
        }else if (selectedReactionIndex == 4){
          reaction = "sad";
        }else if (selectedReactionIndex == 5){
          reaction = "angry";
        }
        selectedReaction.onSelectReaction(reaction);
        onDeselect();
        return true;
    }
    return super.onTouchEvent(event);
  }

  private void onSelect(int index) {
    if (selectedIndex == index) {
      return;
    }

    selectedIndex = index;

    selectingAnimation.prepare();
    startAnimation(selectingAnimation);
  }

  private void onDeselect() {
    deselectAnimation.prepare();
    startAnimation(deselectAnimation);
  }

  @Override
  protected void onDraw(final Canvas canvas) {
    board.draw(canvas);
    for (Emotion emotion : emotions) {
      emotion.draw(canvas);
    }

    postDelayed(runnable, DRAW_DELAY);
  }

  private void animateEmotions(float interpolatedTime) {

    for (Emotion emotion : emotions) {
      animateEmotionSize(emotion, interpolatedTime);
      animateEmotionPosition(emotion);
    }
  }

  private void animateEmotionPosition(Emotion emotion) {
    emotion.y = RoundedBoard.BASE_LINE - emotion.size;

    emotions.get(0).x = RoundedBoard.LEFT + Constants.HORIZONTAL_SPACING;
    emotions.get(emotions.size() - 1).x =
        RoundedBoard.LEFT + RoundedBoard.WIDTH - Constants.HORIZONTAL_SPACING
            - emotions.get(emotions.size() - 1).size;

    for (int i = 1; i < selectedIndex; i++) {
      emotions.get(i).x = emotions.get(i - 1).x + emotions.get(i - 1).size
          + Constants.HORIZONTAL_SPACING;
    }

    for (int i = emotions.size() - 2; i > selectedIndex; i--) {
      emotions.get(i).x = emotions.get(i + 1).x - emotions.get(i).size
          - Constants.HORIZONTAL_SPACING;
    }

    if (selectedIndex > 0) {
      emotions.get(selectedIndex).x = emotions.get(selectedIndex - 1).x
          + emotions.get(selectedIndex - 1).size + Constants.HORIZONTAL_SPACING;
    }
  }

  private void animateEmotionSize(Emotion emotion, float interpolatedTime) {
    emotion.setCurrentSize(emotion.startAnimatedSize +
        (int) (interpolatedTime * (emotion.endAnimatedSize - emotion.startAnimatedSize)));
  }

  private void animateRoundedBoard(float interpolatedTime) {
    board.setCurrentHeight(board.startAnimatedHeight + (interpolatedTime *
        (board.endAnimatedHeight - board.startAnimatedHeight)));
  }

  private class SelectingAnimation extends Animation {

    SelectingAnimation() {
      setDuration(SCALE_DURATION);
    }

    void prepare(){
      prepareEmotions();
      prepareRoundedBoard();
    }

    private void prepareEmotions() {
      for (int i = 0; i < emotions.size(); i++) {
        emotions.get(i).startAnimatedSize = emotions.get(i).size;

        if (i == selectedIndex) {
          emotions.get(i).endAnimatedSize = Emotion.LARGE_SIZE;
        } else {
          emotions.get(i).endAnimatedSize = Emotion.SMALL_SIZE;
        }
      }
    }

    private void prepareRoundedBoard() {
      board.startAnimatedHeight = board.height;
      board.endAnimatedHeight = RoundedBoard.SCALED_DOWN_HEIGHT;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      animateRoundedBoard(interpolatedTime);
      animateEmotions(interpolatedTime);
    }
  }

  private class DeselectAnimation extends Animation {

    DeselectAnimation() {
      setDuration(SCALE_DURATION);
    }

    void prepare(){
      prepareRoundedBoard();
      prepareEmotions();
    }

    private void prepareEmotions() {
      for (Emotion emotion : emotions) {
        emotion.startAnimatedSize = emotion.size;
        emotion.endAnimatedSize = Emotion.MEDIUM_SIZE;
      }
    }

    private void prepareRoundedBoard() {
      board.startAnimatedHeight = board.height;
      board.endAnimatedHeight = RoundedBoard.HEIGHT;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      animateRoundedBoard(interpolatedTime);
      animateEmotions(interpolatedTime);
    }
  }

  private class TranslationAnimation extends Animation {

    private static final int TRANSLATION_DISTANCE = 150;
    private final int EMOTION_RADIUS = Emotion.MEDIUM_SIZE / 2;

    TranslationAnimation() {
      setDuration(TRANSLATION_DURATION);
      prepareRoundedBoard();
      prepareEmotions();
    }

    private void prepareEmotions() {
      for (int i = 0; i < emotions.size(); i++) {
        emotions.get(i).endAnimatedY = RoundedBoard.TOP + Constants.VERTICAL_SPACING;
        emotions.get(i).startAnimatedY =
            emotions.get(i).y = RoundedBoard.BOTTOM + TRANSLATION_DISTANCE;

        emotions.get(i).startAnimatedX
            = emotions.get(i).x = i == 0 ? RoundedBoard.LEFT
            + Constants.HORIZONTAL_SPACING + (Emotion.MEDIUM_SIZE / 2)
            : emotions.get(i - 1).x + Emotion.MEDIUM_SIZE + Constants.HORIZONTAL_SPACING;
      }
    }

    private void prepareRoundedBoard() {
      board.startAnimatedY = board.y = RoundedBoard.BOTTOM + TRANSLATION_DISTANCE;

      board.endAnimatedY = RoundedBoard.TOP;
      board.startAnimatedY = board.y;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      translateEmotions(interpolatedTime);
      translateRoundedBoard();
    }

    private void translateEmotions(float interpolatedTime) {
      float currentTime = interpolatedTime * TRANSLATION_DURATION;
      for (int i = 0; i < emotions.size(); i++) {

        int delayOfChild = CHILD_DELAY * i;

        Emotion view = emotions.get(i);
        if ((currentTime > delayOfChild)) {
          if ((currentTime - delayOfChild) <= CHILD_TRANSLATION_DURATION) {

            float progressOfChild = ((currentTime - delayOfChild) / CHILD_TRANSLATION_DURATION);
            view.y = view.startAnimatedY +
                progressOfChild * (view.endAnimatedY - view.startAnimatedY);

            view.x = view.startAnimatedX - progressOfChild * EMOTION_RADIUS;

            view.setCurrentSize((int) (progressOfChild * Emotion.MEDIUM_SIZE));
          } else {
            view.x = view.startAnimatedX - EMOTION_RADIUS;
            view.y = view.endAnimatedY;
            view.setCurrentSize(Emotion.MEDIUM_SIZE);
          }
        }
      }
    }

    private void translateRoundedBoard() {
      Emotion firstEmoticon = emotions.get(0);
      float d =
          (firstEmoticon.y - firstEmoticon.startAnimatedY) / (firstEmoticon.endAnimatedY
              - firstEmoticon.startAnimatedY) * (board.endAnimatedY - board.startAnimatedY);

      board.y = board.startAnimatedY + d;
    }

  }

  public interface SelectedReaction {
    void onSelectReaction(String selectedReaction);
  }

}
