package tzpace.app.catfactssample.utils.spantext;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

public final class ImageTextFlowSpannable implements LeadingMarginSpan.LeadingMarginSpan2 {

    private final int margin;
    private final int lines;

    public ImageTextFlowSpannable(final int _lines, final int _margin) {
        margin = _margin;
        lines = _lines;
    }

    @Override
    public final int getLeadingMarginLineCount() {
        return lines;
    }

    @Override
    public final int getLeadingMargin(final boolean first) {
        if (first) {
            return margin;
        } else {
            return 0;
        }
    }

    @Override
    public final void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        //what a shit =)
    }

}
