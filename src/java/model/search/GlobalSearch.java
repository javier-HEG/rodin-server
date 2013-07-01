package model.search;

import model.results.ResultEntity;
import model.results.SourceDocumentEntity;

/**
 *
 * @author Javier Belmonte
 */
public class GlobalSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {
		// TODO Implement the real search
		ResultEntity firstResult = new ResultEntity();
		firstResult.setSearch(getSearchEntity());
		firstResult.setTitle("Queueing Theoretic Approaches to Financial Price Fluctuations");
		firstResult.addAuthor("Erhan Bayraktar");
		firstResult.addAuthor("Ulrich Horst");
		firstResult.addAuthor("Ronnie Sircar");

		SourceDocumentEntity document = new SourceDocumentEntity();
		document.setSourceLinkURL("http://arxiv.org/abs/math/0703832");
		documentFacadeREST.create(document);

		firstResult.addDocument(document);
		resultFacadeREST.create(firstResult);

		ResultEntity secondResult = new ResultEntity();
		firstResult.setSearch(getSearchEntity());
		firstResult.setTitle("A Stochastic Feedback Model for Volatility");
		firstResult.addAuthor("Raoul Golan");
		firstResult.addAuthor("Austin Gerig");

		SourceDocumentEntity anotherDocument = new SourceDocumentEntity();
		document.setSourceLinkURL("http://arxiv.org/abs/1306.4975");
		documentFacadeREST.create(anotherDocument);

		secondResult.addDocument(anotherDocument);
		resultFacadeREST.create(secondResult);
	}
}
