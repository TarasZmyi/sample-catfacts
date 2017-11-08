package tzpace.app.catfactssample.communication.dto.json.pagedcatfacts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import tzpace.app.catfactssample.communication.dto.BaseDto;
import tzpace.app.catfactssample.communication.dto.json.catfact.CatFactDto;

@SuppressWarnings("unused")
public final class PagedCatFactsDto extends BaseDto {

    @SerializedName("current_page")
    private int currentPage;

    @SerializedName("last_page")
    private int lastPage;

    @SerializedName("data")
    private List<CatFactDto> data;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<CatFactDto> getData() {
        return data;
    }

    public void setData(List<CatFactDto> data) {
        this.data = data;
    }

}
