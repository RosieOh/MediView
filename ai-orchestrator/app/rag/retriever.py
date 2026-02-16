"""RAG Retriever - ChromaDB-based document retrieval for policy/knowledge base."""

import os
from langchain_community.vectorstores import Chroma
from langchain_openai import OpenAIEmbeddings
from langchain_text_splitters import RecursiveCharacterTextSplitter

CHROMA_PERSIST_DIR = os.getenv("CHROMA_PERSIST_DIR", "./chroma_data")
COLLECTION_NAME = "mediview_policies"

_retriever = None


def get_policy_retriever(k: int = 4):
    """Get or create the policy document retriever."""
    global _retriever

    if _retriever is not None:
        return _retriever

    try:
        embeddings = OpenAIEmbeddings()
        vectorstore = Chroma(
            collection_name=COLLECTION_NAME,
            embedding_function=embeddings,
            persist_directory=CHROMA_PERSIST_DIR,
        )

        # Check if collection has documents
        if vectorstore._collection.count() > 0:
            _retriever = vectorstore.as_retriever(search_kwargs={"k": k})
            return _retriever
        else:
            return None
    except Exception:
        return None


def ingest_documents(documents: list[dict]):
    """Ingest documents into the vector store.

    Args:
        documents: List of dicts with 'content', 'source', 'title' keys.
    """
    embeddings = OpenAIEmbeddings()

    splitter = RecursiveCharacterTextSplitter(
        chunk_size=1000,
        chunk_overlap=200,
        separators=["\n\n", "\n", ".", " "],
    )

    texts = []
    metadatas = []

    for doc in documents:
        chunks = splitter.split_text(doc["content"])
        for chunk in chunks:
            texts.append(chunk)
            metadatas.append({
                "source": doc.get("source", "unknown"),
                "title": doc.get("title", ""),
                "version": doc.get("version", "1.0"),
            })

    vectorstore = Chroma(
        collection_name=COLLECTION_NAME,
        embedding_function=embeddings,
        persist_directory=CHROMA_PERSIST_DIR,
    )

    vectorstore.add_texts(texts=texts, metadatas=metadatas)

    # Reset cached retriever
    global _retriever
    _retriever = None

    return len(texts)
