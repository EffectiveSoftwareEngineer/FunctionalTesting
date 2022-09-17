public class KeepingTestsFocused {
    public void test_create_post_by_new_user_before_change() {
        User user = new User("John", "Doe", new Permissions(REGULAR_USER));
        UserCreateResponse userCreateResponse = userManagementCliend.createUser(user);
        Post post = new Post("Some amazing thing happened to me today you have to know about");
        CreatePostResponse actual = underTest.createPost(post);
        assertThat(actual.StatusCode()).isEqualTo(HTTP_CREATED);
    }

    public void test_create_post_by_new_user_after_change() {
        User user = new User("John", "Doe", new Permissions(REGULAR_USER));
        UserCreateResponse userCreateResponse = userManagementCliend.createUser(user);
        assertThat(userCreateResponse.StatusCode()).isEqualTo(HTTP_CREATED);
        Post post = new Post("Some amazing thing happened to me today you have to know about");
        CreatePostResponse actual = underTest.createPost(post);
        assertThat(actual.StatusCode()).isEqualTo(HTTP_CREATED);
    }
}
