package testClass;

public class NullProp {
	int test;
	NullProp link;
	public NullProp() {

	}

	public void setTest(int num) {
		test = num;
	}

	public int getTest() {
		return test;
	}

	public void link(NullProp other) {
		link = other;
	}

	public NullProp getLink() {
		return link;
	}

	@Override
	public String toString() {
		return Integer.toString(test);
	}
}
