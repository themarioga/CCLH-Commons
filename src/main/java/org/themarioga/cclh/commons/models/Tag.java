package org.themarioga.cclh.commons.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "t_tag")
public class Tag {

	@Id
	private String tag;

	@Column(name = "text", length = 4000, nullable = false)
	private String text;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "lang_id", referencedColumnName = "id", nullable = false)
	private Lang lang;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Lang getLang() {
		return lang;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tag tag1 = (Tag) o;
		return Objects.equals(tag, tag1.tag) && Objects.equals(text, tag1.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tag, text);
	}

	@Override
	public String toString() {
		return "Tag{" +
				"tag='" + tag + '\'' +
				", text='" + text + '\'' +
				'}';
	}

}
